package com.noteapp.ui.ext

import androidx.core.view.isVisible
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

fun StateLoadingBinding.onError(errorMessage: String, tryAgainAction: () -> Unit) {
    tvErrorLoading.text = errorMessage
    btnTryLoadingAgain.setOnClickListener { tryAgainAction() }
    pbLoading.hide()
    tvErrorLoading.isVisible = true
    btnTryLoadingAgain.isVisible = true
}

