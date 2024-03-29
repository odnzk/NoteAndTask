package com.noteapp.ui.ext

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.noteapp.ui.R

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .show()
}

fun View.showSnackbar(
    @StringRes resId: Int,
    @StringRes actionTitleId: Int = R.string.action_undo,
    actionListener:View.OnClickListener? = null
) {
    Snackbar.make(this, resId, Snackbar.LENGTH_SHORT).run {
        actionListener?.let { setAction(actionTitleId, it) }
        show()
    }
}
