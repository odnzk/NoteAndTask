package com.noteapp.feature_todolist.internal.dialog

import com.example.domain.model.Category
import com.noteapp.feature_todolist.internal.TodoPeriodicity
import java.util.*

sealed interface AddTodoDialogEvent {
    object AddTodo : AddTodoDialogEvent
    data class UpdateTitle(val title: String) : AddTodoDialogEvent
//    data class UpdateDeadlineDate(val date: Date) : AddTodoDialogEvent
    data class UpdateDeadlineDate(val date: Date) : AddTodoDialogEvent
    data class UpdateCategory(val category: Category) : AddTodoDialogEvent

    data class UpdateReminderInfo(val calendar: Calendar) : AddTodoDialogEvent
    data class UpdatePeriodInfo(val todoPeriodicity: TodoPeriodicity) : AddTodoDialogEvent // todo

}
