package com.example.unotes.roomdatabase.presentation

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel( private val dao: NoteDao): ViewModel() {

    private val _isSortedByDateAdded = MutableStateFlow(true)
    val isSortedByDateAdded = _isSortedByDateAdded.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val notes = isSortedByDateAdded.flatMapLatest { sortByDate ->
        if (sortByDate) dao.getnotesOrderedByDateAdded()
        else dao.getnotesOrderedByTitle()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow(NoteState())
    val state = combine(_state,isSortedByDateAdded, notes) { state,isSortedByDateAdded, notes ->
        state.copy(notes = notes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.SaveNote -> {
                val note = Note(
                    title = event.title,
                    description = event.description,
                    dateAdded = System.currentTimeMillis(),
                    timestamp = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsertNote(note)
                }
                clearInputs()
            }

            is NoteEvent.UpdateNote -> {
                viewModelScope.launch {
                    val existingNote = dao.getNoteById(event.noteId)
                    existingNote?.let {
                        val updatedNote = it.copy(
                            title = event.title,
                            description = event.description,
                            timestamp = System.currentTimeMillis()
                        )
                        dao.updateNote(updatedNote)
                    }
                }
                clearInputs()
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            NoteEvent.SortNotes -> {
                _isSortedByDateAdded.value = !_isSortedByDateAdded.value
            }
        }
    }

    private fun clearInputs() {
        _state.value = _state.value.copy(
            title = mutableStateOf(""),
            description = mutableStateOf("")
        )
    }
}