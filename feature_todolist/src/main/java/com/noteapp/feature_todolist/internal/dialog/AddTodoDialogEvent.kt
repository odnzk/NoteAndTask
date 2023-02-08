package com.noteapp.feature_todolist.internal.dialog

import com.example.domain.model.Category
import com.noteapp.feature_todolist.internal.TodoPeriodicity
import java.util.*

sealed interface AddTodoDialogEvent {
    object AddTodo : AddTodoDialogEvent
    data class UpdateTitle(val title: String) : AddTodoDialogEvent

    data class UpdateDeadline(val date: Date) : AddTodoDialogEvent
    data class UpdateCategory(val category: Category) : AddTodoDialogEvent

    data class UpdateReminder(val calendar: Calendar) : AddTodoDialogEvent
    data class UpdatePeriodicity(val todoPeriodicity: TodoPeriodicity) : AddTodoDialogEvent // todo

}
