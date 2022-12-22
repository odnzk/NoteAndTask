package com.example.noteapp.ui.viewmodel


sealed class ListState<T>(val data: T? = null, val error: Throwable? = null) {
    class Loading<T> : ListState<T>()
    class Success<T>(data: T) : ListState<T>(data)
    class Error<T>(error: Throwable) : ListState<T>(error = error)
}

// separated because ListState can be modified, so here will be "implementation" of work with him
fun <T> ListState<T>.handleState(
    onLoadingAction: () -> Unit,
    onErrorAction: (Throwable) -> Unit,
    onSuccessAction: (T) -> Unit
) {
    when (this) {
        is ListState.Loading -> onLoadingAction()
        is ListState.Error -> error?.let { onErrorAction(it) }
        is ListState.Success -> data?.let { onSuccessAction(it) }
    }
}
// Loading: holds nothing
// Error: holds throwable/exception and etc
// Success: hold some successfully loaded data
// Exceptions:
// 1) SQlConstraint exception
// 2) Nothing was find like 404


