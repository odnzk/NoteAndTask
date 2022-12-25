package com.example.noteapp.ui.fragments.events

import com.example.domain.model.Note

sealed interface NoteDetailedEvent {
    data class UpdateNote(val note: Note) : NoteDetailedEvent
    object TryLoadingNoteAgain : NoteDetailedEvent
    object DeleteNote : NoteDetailedEvent
}
