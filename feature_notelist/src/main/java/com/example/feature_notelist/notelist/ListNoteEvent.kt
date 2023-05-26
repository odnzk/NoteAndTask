package com.example.feature_notelist.notelist

import com.example.domain.model.Note
import com.example.domain.model.NoteSortOrder

internal sealed interface ListNoteEvent {
    class DeleteNote(val note: Note) : ListNoteEvent
    class UpdateSortOrder(val noteSortOrder: NoteSortOrder) : ListNoteEvent
    object RestoreNote : ListNoteEvent
    object DeleteAllNotes : ListNoteEvent
    object Reload : ListNoteEvent
}
