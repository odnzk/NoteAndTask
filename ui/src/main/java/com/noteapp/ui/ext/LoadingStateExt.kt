package com.noteapp.ui.ext

import androidx.core.view.isVisible
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import com.example.noteapp.ui.util.exceptions.LoadingFromDatabaseFailedException
import com.example.noteapp.ui.util.exceptions.NotFoundException
import com.noteapp.ui.R
import com.noteapp.ui.databinding.StateLoadingBinding

fun StateLoadingBinding.loadingFinished() {
    pbLoading.hide()
    tvErrorLoading.isVisible = false
    btnTryLoadingAgain.isVisible = false
}

fun StateLoadingBinding.loadingStarted() {
    pbLoading.show()
    tvErrorLoading.isVisible = false
    btnTryLoadingAgain.isVisible = false
}

fun StateLoadingBinding.errorOccurred(error: Throwable, tryAgainAction: () -> Unit) {
    val resId = when (error) {
        is NotFoundException -> R.string.error_not_found
        is LoadingFromDatabaseFailedException -> R.string.error_failed_loading_from_database
        is InvalidNavArgumentsException -> R.string.error_navigation_exception
        else -> R.string.error_unknown
    }
    tvErrorLoading.text = root.context.getString(resId)
    btnTryLoadingAgain.setOnClickListener {
        tryAgainAction()
    }
    pbLoading.hide()
    tvErrorLoading.isVisible = true
    btnTryLoadingAgain.isVisible = true
}

