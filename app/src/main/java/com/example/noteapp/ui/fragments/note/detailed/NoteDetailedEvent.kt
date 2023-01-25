package com.example.noteapp.ui.fragments.note.detailed

import com.noteapp.model.Note

sealed interface NoteDetailedEvent {
    data class UpdateNote(val note: Note) : NoteDetailedEvent
    object TryLoadingNoteAgain : NoteDetailedEvent
    object DeleteNote : NoteDetailedEvent
}
