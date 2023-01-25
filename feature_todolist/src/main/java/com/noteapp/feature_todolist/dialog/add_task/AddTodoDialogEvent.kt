package com.noteapp.feature_todolist.dialog.add_task

import com.noteapp.model.Category
import java.util.*

sealed interface AddTodoDialogEvent {
    object AddTodo : AddTodoDialogEvent
    data class UpdateTitle(val title: String) : AddTodoDialogEvent
    data class UpdateDeadlineDate(val date: Date) : AddTodoDialogEvent
    data class UpdateCategory(val category: Category) : AddTodoDialogEvent

    data class UpdateReminderInfo(val calendar: Calendar) : AddTodoDialogEvent
    data class UpdatePeriodInfo(val periodInfo: String) : AddTodoDialogEvent // todo

}
