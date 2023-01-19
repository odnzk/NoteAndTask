package com.example.noteapp.ui.util.ext

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

fun Spinner.init(
    array: Array<String>,
    @LayoutRes customSpinnerLayoutRes: Int,
    @IdRes tvContentId: Int
) {
    val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
        context,
        customSpinnerLayoutRes,
        tvContentId,
        array
    )
    adapter = spinnerAdapter
}

fun Spinner.init(
    array: Array<String>,
) {
    ArrayAdapter(
        context,
        android.R.layout.simple_spinner_item,
        array
    ).also {
        adapter = it
    }
}



