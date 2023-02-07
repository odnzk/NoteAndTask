package com.noteapp.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.noteapp.core.state.UiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

interface UiStateObserver {

    suspend fun <T> StateFlow<UiState<T>>.collectState(
        lifecycleOwner: LifecycleOwner,
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onLoading: () -> Unit
    ) {
        lifecycleOwner.repeatOnLifecycle(lifecycleState) {
            this@collectState.collectLatest {
                when (it) {
                    is UiState.Error -> it.error?.let { onError(it) }
                    is UiState.Loading -> onLoading()
                    is UiState.Success -> it.data?.let { onSuccess(it) }
                }
            }
        }
    }
}
