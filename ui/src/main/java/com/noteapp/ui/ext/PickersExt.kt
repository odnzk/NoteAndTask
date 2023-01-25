package com.example.noteapp.ui.util.ext

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

fun Context.showDatePicker(onDateSelected: (DatePicker, Int, Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        this,
        { datePicker, year, month, day ->
            onDateSelected(datePicker, year, month, day)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun Context.showTimePicker(onTimeSelected: (TimePicker, Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        this,
        { timePicker, hourOfTheDay, minute ->
            onTimeSelected(timePicker, hourOfTheDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false,
    ).show()
}

fun Context.showDateTimePicker(onDateTimeSelected: (Calendar) -> Unit) {
    val calendar: Calendar = Calendar.getInstance()
    showDatePicker { datePicker, year, month, day ->
        calendar.run {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        showTimePicker { timePicker, hourOfDay, minute ->
            calendar.run {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                onDateTimeSelected(calendar)
            }
        }
    }
}
