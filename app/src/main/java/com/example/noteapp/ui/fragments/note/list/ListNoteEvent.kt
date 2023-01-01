package com.example.noteapp.ui.fragments.note.list

sealed interface ListNoteEvent {
    object ClearAll : ListNoteEvent
    object TryAgain : ListNoteEvent
}
