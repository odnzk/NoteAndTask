package com.example.domain.util.sorting

import com.example.domain.model.Todo
import com.noteapp.core.ext.isSameDay
import com.noteapp.core.ext.toCalendar
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
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

object ThisWeekSorter : TodoSorter {
    override fun sort(items: List<Todo>): List<Todo> {
        return items
            .filter { it.deadlineDate != null }
            .filter { todo ->
                val today = LocalDate.now(ZoneId.systemDefault())
                val previousMonday: LocalDate =
                    today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
                val nextMonday: LocalDate =
                    today.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                val date: LocalDate =
                    Instant.ofEpochMilli(todo.deadlineDate!!.time).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                (previousMonday <= date) && (nextMonday > date)
            }
    }
}
