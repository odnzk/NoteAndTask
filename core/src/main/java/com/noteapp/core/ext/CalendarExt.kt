package com.noteapp.core.ext

import java.util.*

fun Calendar.isSameDay(other: Calendar): Boolean {
    return get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH)
            && get(Calendar.MONTH) == other.get(Calendar.MONTH)
            && get(Calendar.YEAR) == other.get(Calendar.YEAR)
}

fun Date.toCalendar(): Calendar {
    return Calendar.getInstance(Locale.getDefault()).apply {
        time = this@toCalendar
    }
}

fun Calendar.setStartOfTheDay() = apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun Calendar.isFuture(): Boolean = Calendar.getInstance() < this

