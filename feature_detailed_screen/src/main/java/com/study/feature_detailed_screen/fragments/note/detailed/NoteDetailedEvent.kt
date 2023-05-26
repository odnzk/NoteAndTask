package com.study.feature_detailed_screen.fragments.note.detailed

import com.example.domain.model.Note

internal sealed interface NoteDetailedEvent {
    class UpdateNote(val note: Note) : NoteDetailedEvent
    object Reload : NoteDetailedEvent
    object DeleteNote : NoteDetailedEvent
}
