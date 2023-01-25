package com.example.feature_notelist.navigation

import androidx.navigation.NavController
import com.example.feature_notelist.NotesListFragmentDirections

internal fun NavController.toDetailedNote(noteId: Long? = null) {
    noteId?.let {
        navigate(
            NotesListFragmentDirections.actionNotesListFragmentToNoteDetailFragment(
                it
            )
        )
    } ?: navigate(NotesListFragmentDirections.actionNotesListFragmentToNoteDetailFragmentCreate())
    // null -> create note
    // not null -> open existing note
}
