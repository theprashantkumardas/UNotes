package com.example.unotes.roomdatabase.presentation

import com.example.unotes.roomdatabase.data.Note

sealed interface NoteEvent {
    object SortNotes : NoteEvent

    data class DeleteNote(val note: Note) : NoteEvent

    data class SaveNote(val title: String, val description: String) : NoteEvent

    data class UpdateNote(val noteId: Int, val title: String, val description: String) : NoteEvent

    data class AddImage(val uri: List<String>) : NoteEvent
    data class AddVideo(val uri: List<String>) : NoteEvent
    data class UpdateDescription(val text: String) : NoteEvent // Added this

    object ClearState : NoteEvent

}
