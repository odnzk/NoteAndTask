package com.example.noteapp.ui.util.ext

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(message: String) {
    Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        .show()
}
