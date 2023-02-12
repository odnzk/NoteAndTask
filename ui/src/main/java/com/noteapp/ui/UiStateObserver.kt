package com.noteapp.ui

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.noteapp.core.state.UiState
import com.noteapp.ui.ext.HandledError
import com.noteapp.ui.ext.handleException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> StateFlow<UiState<T>>.collectAsUiState(
    context: Context?,
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onSuccess: (T) -> Unit,
    onError: (HandledError) -> Unit,
    onLoading: () -> Unit
) = lifecycleOwner.repeatOnLifecycle(lifecycleState) {
    this@collectAsUiState.collectLatest {
        when (it) {
            is UiState.Loading -> onLoading()
            is UiState.Success -> it.data?.let { onSuccess(it) }
            is UiState.Error -> it.error?.let { throwable ->
                onError(context?.handleException(throwable) ?: HandledError(error = throwable))
            }
        }
    }
}


