package com.noteapp.core.state


sealed class UiState<T>(val data: T? = null, val error: Throwable? = null) {
    class Loading<T> : UiState<T>()
    class Success<T>(data: T) : UiState<T>(data)
    class Error<T>(error: Throwable) : UiState<T>(error = error)
}
fun <T> UiState<T>.handleState(
    onLoadingAction: () -> Unit,
    onErrorAction: (Throwable) -> Unit,
    onSuccessAction: (T) -> Unit
) {
    when (this) {
        is UiState.Loading -> onLoadingAction()
        is UiState.Error -> error?.let { onErrorAction(it) }
        is UiState.Success -> data?.let { onSuccessAction(it) }
    }
}


