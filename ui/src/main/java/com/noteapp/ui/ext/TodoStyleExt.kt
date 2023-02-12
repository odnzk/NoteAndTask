package com.noteapp.ui.ext

import android.content.res.ColorStateList
import android.graphics.Paint
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.domain.model.Category
import com.example.domain.model.Todo
import com.noteapp.core.ext.isSameDay
import com.noteapp.core.ext.toCalendar
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
fun TextView.setTodoDateStyle(todo: Todo, ivIcon: ImageView? = null) {
    todo.deadlineDate?.let { deadlineDate ->
        val currentDate = Calendar.getInstance()
        val todoDateInCalendar = deadlineDate.toCalendar()

        val colorResIdWithTitle: Pair<Int, String> =
            if (currentDate.isSameDay(todoDateInCalendar)) {
                R.color.todo_item_today_blue to context.getString(R.string.deadline_today)
            } else if (deadlineDate.time < currentDate.timeInMillis && !todo.isCompleted) {
                R.color.todo_item_overdue_red to deadlineDate.formatToTodoDate()
            } else {
                R.color.black to deadlineDate.formatToTodoDate()
            }

        val color = context.getColor(colorResIdWithTitle.first)
        setTextColor(color)
        ivIcon?.let { it.imageTintList = ColorStateList.valueOf(color) }
        text = colorResIdWithTitle.second
    }
}

fun CheckBox.setCategoryColor(category: Category?) {
    category?.let { buttonTintList = ColorStateList.valueOf(it.color) }
}




