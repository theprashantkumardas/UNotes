package com.example.unotes.roomdatabase.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unotes.roomdatabase.data.NoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.unotes.roomdatabase.data.Note
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel(private val dao: NoteDao) : ViewModel() {

    private val _isSortedByDateAdded = MutableStateFlow(true)
    val isSortedByDateAdded = _isSortedByDateAdded.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val notes = isSortedByDateAdded.flatMapLatest { sortByDate ->
        if (sortByDate) dao.getnotesOrderedByDateAdded()
        else dao.getnotesOrderedByTitle()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow(NoteState())
    val state = combine(_state, isSortedByDateAdded, notes) { state, isSortedByDateAdded, notes ->
        state.copy(notes = notes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    private val _addEditState = MutableStateFlow(AddEditNoteState())
    val addEditState: StateFlow<AddEditNoteState> = _addEditState.asStateFlow()


    private var currentDescriptionItems: MutableList<DescriptionItem> =
        mutableListOf(DescriptionItem.TextItem(""))


    init {
        currentDescriptionItems = mutableListOf(DescriptionItem.TextItem(""))
    }


    private fun onImageSelected(uri: List<String>) {
        _addEditState.update { it.copy(imageUris = uri) }
        val newItem = DescriptionItem.ImageItem(uri.first())
        currentDescriptionItems.add(newItem)
        _addEditState.update { it ->
            it.copy(descriptionItems = currentDescriptionItems)
        }
    }

    private fun onVideoSelected(uri: List<String>) {
        _addEditState.update { it.copy(videoUris = uri) }
        val newItem = DescriptionItem.VideoItem(uri.first())
        currentDescriptionItems.add(newItem)
        _addEditState.update { it ->
            it.copy(descriptionItems = currentDescriptionItems)
        }
    }


    fun updateDescription(text: String) {
        if(currentDescriptionItems.isNotEmpty() && currentDescriptionItems.last() is DescriptionItem.TextItem)
        {
            val lastItem = currentDescriptionItems.removeLast() as DescriptionItem.TextItem
            currentDescriptionItems.add(DescriptionItem.TextItem(lastItem.text + text))

        } else{
            currentDescriptionItems.add(DescriptionItem.TextItem(text))
        }
        _addEditState.update { it ->
            it.copy(descriptionItems = currentDescriptionItems)
        }
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.SaveNote -> {
                _addEditState.update { it.copy(isLoading = true, error = null) }
                val note = Note(
                    title = event.title,
                    description = getUpdatedDescription(),
                    imageUris = _addEditState.value.imageUris, // Use selected image URIs
                    videoUris = _addEditState.value.videoUris, // Use selected video URIs
                    dateAdded = System.currentTimeMillis(),
                    timestamp = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    try {
                        dao.upsertNote(note)
                        _addEditState.update { it.copy(isLoading = false) }
                        clearInputs()
                    }catch (e:Exception){
                        _addEditState.update { it.copy(isLoading = false, error = e.message ) }
                    }

                }
            }

            is NoteEvent.UpdateNote -> {
                _addEditState.update { it.copy(isLoading = true, error = null) }
                viewModelScope.launch {
                    try {
                        val existingNote = dao.getNoteById(event.noteId)
                        existingNote?.let {
                            val updatedNote = it.copy(
                                title = event.title,
                                description = getUpdatedDescription(),
                                imageUris = _addEditState.value.imageUris,
                                videoUris = _addEditState.value.videoUris,
                                timestamp = System.currentTimeMillis()
                            )
                            dao.updateNote(updatedNote)
                            _addEditState.update { it.copy(isLoading = false) }

                        }
                    } catch (e:Exception){
                        _addEditState.update { it.copy(isLoading = false, error = e.message ) }
                    }

                }
                clearInputs()
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            is NoteEvent.AddImage ->{
                onImageSelected(event.uri)
            }
            is NoteEvent.AddVideo -> {
                onVideoSelected(event.uri)
            }
            is NoteEvent.ClearState ->{
                _addEditState.update { it.copy(descriptionItems = mutableListOf(DescriptionItem.TextItem(""))) }
                currentDescriptionItems =
                    mutableListOf(DescriptionItem.TextItem(""))

            }

            NoteEvent.SortNotes -> {
                _isSortedByDateAdded.value = !_isSortedByDateAdded.value
            }

            is NoteEvent.UpdateDescription -> TODO()
        }
    }

    private fun getUpdatedDescription(): String {
        return currentDescriptionItems.joinToString(separator = "") { item ->
            when (item) {
                is DescriptionItem.TextItem -> item.text
                is DescriptionItem.ImageItem -> "<image>${item.uri}</image>"
                is DescriptionItem.VideoItem -> "<video>${item.uri}</video>"
            }
        }
    }
    private fun clearInputs() {
        _state.value = _state.value.copy(
            title = mutableStateOf(""),
            description = mutableStateOf("")
        )
        currentDescriptionItems =
            mutableListOf(DescriptionItem.TextItem(""))
        _addEditState.update { AddEditNoteState() }
    }
}