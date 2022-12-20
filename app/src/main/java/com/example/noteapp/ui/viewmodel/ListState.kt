package com.example.noteapp.ui.viewmodel


sealed interface ListState {
    object Loading : ListState
    data class Success<T>(val data: T) : ListState
    data class Error(val error: Throwable) : ListState
}
// Loading: holds nothing
// Error: holds throwable/exception and etc
// Success: hold some successfully loaded data

// Exceptions:
// 1) SQlConstraint exception
// 2) Nothing was find like 404


