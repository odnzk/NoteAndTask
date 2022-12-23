package com.example.noteapp.ui.util

import androidx.core.view.isVisible
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.util.exceptions.LoadingFromDatabaseFailedException
import com.example.noteapp.ui.util.exceptions.NotFoundException

fun StateLoadingBinding.loadingFinished() {
    pbLoading.hide()
    tvErrorLoading.isVisible = false
    btnTryLoadingAgain.isVisible = false
//    this.root.isVisible = false
}

fun StateLoadingBinding.loadingStarted() {
    pbLoading.show()
    tvErrorLoading.isVisible = false
    btnTryLoadingAgain.isVisible = false
}

fun StateLoadingBinding.errorOccurred(error: Throwable, tryAgainAction: () -> Unit) {
    val resId = when (error) {
        is NotFoundException -> com.example.noteapp.R.string.error_not_found
        is LoadingFromDatabaseFailedException -> com.example.noteapp.R.string.error_failed_loading_from_database
        else -> com.example.noteapp.R.string.error_unknown
    }
    tvErrorLoading.text = root.context.getString(resId)
    btnTryLoadingAgain.setOnClickListener {
        tryAgainAction()
    }
    pbLoading.hide()
    tvErrorLoading.isVisible = true
    btnTryLoadingAgain.isVisible = true
}

