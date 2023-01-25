package com.noteapp.ui.ext

import android.content.res.ColorStateList
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import com.noteapp.core.isSameDay
import com.noteapp.ui.R
import java.util.*

// completed/not completed style
fun TextView.setTodoIsCompletedStyle(isCompleted: Boolean) {
    paintFlags = if (isCompleted) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

// overdue
// today
// todo improve
fun TextView.setTodoDateStyle(todoDate: Date, ivIcon: ImageView? = null) {
    val currentDate = Calendar.getInstance()
    val todoDateInCalendar = Calendar.getInstance().apply { time = todoDate }
    if (currentDate.isSameDay(todoDateInCalendar)) {
        context.getColor(R.color.todo_item_today_blue).also { color ->
            setTextColor(color)
            ivIcon?.let { it.imageTintList = ColorStateList.valueOf(color) }
        }
        text = context.getString(R.string.deadline_today)
    } else if (todoDate.time < currentDate.timeInMillis) {
        context.getColor(R.color.todo_item_overdue_red).also { color ->
            setTextColor(color)
            ivIcon?.let { it.imageTintList = ColorStateList.valueOf(color) }
        }
        text = todoDate.formatToTodoDate()
    } else {
        context.getColor(R.color.black).also { color ->
            setTextColor(color)
            ivIcon?.let { it.imageTintList = ColorStateList.valueOf(color) }
        }
        text = todoDate.formatToTodoDate()
    }
//    if (currentDate.get(Calendar.DAY_OF_MONTH) == todoDateInCalendar.get(Calendar.DAY_OF_MONTH)
//        && currentDate.get(Calendar.MONTH) == todoDateInCalendar.get(Calendar.MONTH)
//        && currentDate.get(Calendar.YEAR) == todoDateInCalendar.get(Calendar.YEAR)
//    ) {
//        context.getColor(R.color.todo_item_today_blue).also { color ->
//            setTextColor(color)
//            ivIcon?.let { it.imageTintList = ColorStateList.valueOf(color) }
//        }
//        text = context.getString(R.string.deadline_today)
//    } else if (todoDate.time < currentDate.timeInMillis) {
//        context.getColor(R.color.todo_item_overdue_red).also { color ->
//            setTextColor(color)
//            ivIcon?.let { it.imageTintList = ColorStateList.valueOf(color) }
//        }
//        text = todoDate.formatToTodoDate()
//    } else {
//        context.getColor(R.color.black).also { color ->
//            setTextColor(color)
//            ivIcon?.let { it.imageTintList = ColorStateList.valueOf(color) }
//        }
//        text = todoDate.formatToTodoDate()
//    }
}




