package com.example.domain.util.sorting

import com.noteapp.core.ext.isFuture
import com.noteapp.core.ext.isSameDay
import com.noteapp.core.ext.setStartOfTheDay
import com.noteapp.core.ext.toCalendar
import com.noteapp.model.Todo
import java.util.*

sealed interface TodoSorter {
    fun sort(items: List<Todo>): List<Todo>
    // DEFAULT, BY_DEADLINE, TODAY, THIS_WEEK

    object ByDeadlineSorter : TodoSorter {
        // todo
        override fun sort(items: List<Todo>): List<Todo> {
            return items.filter { it.deadlineDate != null }.sortedBy { it.deadlineDate }
        }
    }

    object OnlyTodaySorter : TodoSorter {
        override fun sort(items: List<Todo>): List<Todo> {
            val calendar = Calendar.getInstance(Locale.getDefault())
            return items.filter { todo ->
                todo.deadlineDate?.toCalendar()?.isSameDay(calendar) == true
            }
        }
    }


    object ThisWeekSorter : TodoSorter {
        override fun sort(items: List<Todo>): List<Todo> {
            return items
                .filter { it.deadlineDate != null }
                .filter { todo ->
                    val weekStart = getWeekStart()
                    val weekEnd = weekStart.apply { add(Calendar.WEEK_OF_MONTH, 1) }.timeInMillis
                    todo.deadlineDate!!.time in getWeekStart().timeInMillis until weekEnd
                }
        }

        private fun getWeekStart(): Calendar {
            return Calendar.getInstance(Locale.getDefault()).apply {
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                setStartOfTheDay()
                if (isFuture()) {
                    add(Calendar.WEEK_OF_MONTH, -1)
                }
            }
        }
    }
}
