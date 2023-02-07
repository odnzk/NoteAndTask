package com.noteapp.feature_detailedscreens.internal.fragments.note.detailed

import com.example.domain.model.Note

internal sealed interface NoteDetailedEvent {
    data class UpdateNote(val note: Note) : NoteDetailedEvent
    object TryLoadingNoteAgain : NoteDetailedEvent
    object DeleteNote : NoteDetailedEvent
}
