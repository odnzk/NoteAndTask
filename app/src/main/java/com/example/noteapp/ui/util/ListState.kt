package com.example.noteapp.ui.util

import com.example.domain.model.NoteItem

sealed interface ListState {
    object Loading : ListState
    data class Success(val list: List<NoteItem>) : ListState
    data class Error(val error: Throwable) : ListState
}
// Loading: holds nothing
// Error: holds throwable/exception and etc
// Success: hold some successfully loaded data

// Exceptions:
// 1) SQlConstraint exception
// 2) Nothing was find like 404


