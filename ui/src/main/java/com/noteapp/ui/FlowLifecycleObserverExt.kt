package com.noteapp.ui

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.noteapp.ui.ext.HandledError
import com.noteapp.ui.ext.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> Flow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onObserve: (T) -> Unit
) = lifecycleOwner.repeatOnLifecycle(lifecycleState) {
    this@observeWithLifecycle.collectLatest { onObserve(it) }
}


suspend fun <T> StateFlow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onObserve: (T) -> Unit
) = lifecycleOwner.repeatOnLifecycle(lifecycleState) {
    this@observeWithLifecycle.collectLatest { onObserve(it) }
}


suspend fun <T> StateFlow<Result<T>>.observeWithLifecycle(
    context: Context?,
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onSuccess: (T) -> Unit,
    onError: (HandledError) -> Unit
) = lifecycleOwner.repeatOnLifecycle(lifecycleState) {
    this@observeWithLifecycle.collectLatest { result ->
        result.fold(onSuccess = { onSuccess(it) }, onFailure = {
            onError(context?.handleException(it) ?: HandledError(it))
        })
    }
}
