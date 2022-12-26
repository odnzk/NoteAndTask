package com.example.noteapp.ui.fragments.events

sealed interface ListNoteEvent {
    object ClearAll : ListNoteEvent
    object TryAgain : ListNoteEvent
}
