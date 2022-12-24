package com.example.noteapp.ui.util.ext

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.*

fun Context.showDatePicker(onDateSelected: (DatePicker, Int, Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog =
        DatePickerDialog(
            this,
            { datePicker, year, month, day ->
                onDateSelected(datePicker, year, month, day)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    datePickerDialog.show()
}
