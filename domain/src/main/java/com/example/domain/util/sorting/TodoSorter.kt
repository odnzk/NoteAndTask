package com.example.domain.util.sorting

import com.example.domain.model.Todo
import com.noteapp.core.ext.isFuture
import com.noteapp.core.ext.isSameDay
import com.noteapp.core.ext.setStartOfTheDay
import com.noteapp.core.ext.toCalendar
import java.util.*

//  NO_PERIOD, TODAY, TOMORROW, THIS_WEEK
sealed interface TodoSorter {
    fun sort(items: List<Todo>): List<Todo>
}

object DefaultSorter : TodoSorter {
    override fun sort(items: List<Todo>): List<Todo> {
        // without deadline will be last
        return items.sortedWith { t1, t2 ->
            if (t1.deadlineDate == null && t2.deadlineDate == null) return@sortedWith 0
            if (t1.deadlineDate == null) return@sortedWith 1
            if (t2.deadlineDate == null) return@sortedWith -1
            (t1.deadlineDate!!.time - t2.deadlineDate!!.time).toInt()
        }
    }
}

object TodaySorter : TodoSorter {
    override fun sort(items: List<Todo>): List<Todo> {
        val calendar = Calendar.getInstance(Locale.getDefault())
        return items.filter { todo ->
            todo.deadlineDate?.toCalendar()?.isSameDay(calendar) == true
        }
    }
}

object TomorrowSorter : TodoSorter {
    override fun sort(items: List<Todo>): List<Todo> {
        val calendar = Calendar.getInstance(Locale.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }
        return items.filter { todo ->
            todo.deadlineDate?.toCalendar()?.isSameDay(calendar) == true
        }
    }

}

// todo fix!!!
object ThisWeekSorter : TodoSorter {
    override fun sort(items: List<Todo>): List<Todo> {
        return items
            .filter { it.deadlineDate != null }
            .filter { todo ->
                val weekStart = getWeekStart()
                val weekEnd = weekStart.apply {
                    add(Calendar.WEEK_OF_MONTH, 1)
                }.timeInMillis
                todo.deadlineDate!!.time in weekStart.timeInMillis until weekEnd
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
