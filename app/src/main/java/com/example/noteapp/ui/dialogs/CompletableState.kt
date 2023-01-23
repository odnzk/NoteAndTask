package com.example.noteapp.ui.dialogs

sealed class CompletableState<T>(val data: T, val error: Throwable? = null) {
    class Error<T>(data: T, error: Throwable) : CompletableState<T>(data, error)
    class InProgress<T>(data: T) : CompletableState<T>(data)
    class Completed<T>(data: T) : CompletableState<T>(data)
    // error (error : throwable, data: T)
    // progress (data: T)
    // completed
}
