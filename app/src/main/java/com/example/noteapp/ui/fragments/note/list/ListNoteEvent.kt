package com.example.noteapp.ui.fragments.note.list

import com.example.domain.model.Note

sealed interface ListNoteEvent {
    object ClearAll : ListNoteEvent
    object TryAgain : ListNoteEvent

    object RestoreItem : ListNoteEvent
    data class DeleteItem(val note: Note) : ListNoteEvent
}
