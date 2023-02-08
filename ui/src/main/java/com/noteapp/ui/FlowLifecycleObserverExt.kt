package com.noteapp.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> Flow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onObserve: (T) -> Unit
) = lifecycleOwner.repeatOnLifecycle(lifecycleState) {
    this@observeWithLifecycle.collectLatest { onObserve(it) }
}
