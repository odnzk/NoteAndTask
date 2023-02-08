package com.example.feature_notelist.internal

import com.example.domain.model.Note
import com.example.domain.model.NoteSortOrder

internal sealed interface ListNoteEvent {
    data class DeleteNote(val note: Note) : ListNoteEvent
    object RestoreNote : ListNoteEvent

    data class UpdateSortOrder(val noteSortOrder: NoteSortOrder) : ListNoteEvent
    object DeleteAllNotes : ListNoteEvent
    object Reload : ListNoteEvent
}
