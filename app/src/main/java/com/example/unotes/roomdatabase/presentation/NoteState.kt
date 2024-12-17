package com.example.unotes.roomdatabase.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.unotes.roomdatabase.data.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf(""),
    val imageUris: List<String>? = null,  // Nullable for optional images
    val videoUris: List<String>? = null,  // Nullable for optional videos
    val timestamp: MutableState<String> = mutableStateOf(""),


)

data class AddEditNoteState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val imageUris: List<String> = emptyList(), // Added: List of selected image URIs
    val videoUris: List<String> = emptyList(), // Added: List of selected video URIs
)

sealed class DescriptionItem {
    data class TextItem(val text: String) : DescriptionItem()
    data class ImageItem(val uri: String) : DescriptionItem()
    data class VideoItem(val uri: String) : DescriptionItem()
}