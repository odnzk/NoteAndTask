package com.noteapp.feature_todolist.dialog

import com.example.domain.model.Category
import java.util.Calendar
import java.util.Date

internal sealed interface AddTodoDialogEvent {
    object AddTodo : AddTodoDialogEvent
    class UpdateTitle(val title: String) : AddTodoDialogEvent

    class UpdateDeadline(val date: Date) : AddTodoDialogEvent
    class UpdateCategory(val category: Category) : AddTodoDialogEvent

    class UpdateReminder(val calendar: Calendar) : AddTodoDialogEvent

}
