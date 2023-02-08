package com.noteapp.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.noteapp.core.state.CompletableState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> StateFlow<CompletableState<T>>.collectAsUiState(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onComplete: (T) -> Unit,
    onError: (Throwable) -> Unit,
    onProgress: () -> Unit
) =
    lifecycleOwner.repeatOnLifecycle(lifecycleState) {
        this@collectAsUiState.collectLatest {
            when (it) {
                is CompletableState.InProgress -> onProgress()
                is CompletableState.Completed -> it.data?.let { onComplete(it) }
                is CompletableState.Error -> it.error?.let { onError(it) }
            }
        }
    }
