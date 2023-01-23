package com.example.domain.util.exceptions

import java.util.*

fun Calendar.isSameDay(other: Calendar): Boolean {
    return get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH)
            && get(Calendar.MONTH) == other.get(Calendar.MONTH)
            && get(Calendar.YEAR) == other.get(Calendar.YEAR)
}
