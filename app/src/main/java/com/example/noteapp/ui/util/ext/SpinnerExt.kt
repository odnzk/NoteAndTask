package com.example.noteapp.ui.util.ext

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

fun Spinner.init(
    array: Array<String>
) {
    ArrayAdapter(
        context,
        android.R.layout.simple_spinner_item,
        array
    ).also {
        it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter = it
    }
}




