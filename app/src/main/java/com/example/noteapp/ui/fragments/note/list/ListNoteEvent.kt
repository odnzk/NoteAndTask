package com.example.noteapp.ui.fragments.note.list

import com.example.domain.model.Note
import com.example.domain.model.NoteSortOrder

sealed interface ListNoteEvent {
    data class DeleteItem(val note: Note) : ListNoteEvent
    data class UpdateSortOrder(val noteSortOrder: NoteSortOrder) : ListNoteEvent

    object ClearAll : ListNoteEvent
    object TryAgain : ListNoteEvent
    object RestoreItem : ListNoteEvent
}
