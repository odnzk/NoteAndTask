package com.example.feature_notelist.internal

import com.noteapp.model.Note
import com.noteapp.model.NoteSortOrder

internal sealed interface ListNoteEvent {
    data class DeleteItem(val note: Note) : ListNoteEvent
    data class UpdateSortOrder(val noteSortOrder: NoteSortOrder) : ListNoteEvent

    object ClearAll : ListNoteEvent
    object TryAgain : ListNoteEvent
    object RestoreItem : ListNoteEvent
}
