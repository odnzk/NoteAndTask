package com.noteapp.ui.ext

import android.widget.ArrayAdapter
import android.widget.Spinner

fun Spinner.initValues(
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




