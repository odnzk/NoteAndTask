package com.example.noteapp.ui.fragments.todo.list

import com.example.domain.model.Todo

sealed interface ListTodoEvent {
    data class AddItem(val todo: Todo) : ListTodoEvent
    data class DeleteItem(val todo: Todo) : ListTodoEvent
    object ClearAll : ListTodoEvent
    object TryAgain : ListTodoEvent
    object RestoreItem : ListTodoEvent
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) : ListTodoEvent
}
