package com.example.noteapp.ui.fragments.todo.list

import com.example.domain.model.Todo
import com.example.domain.model.TodoSortOrder

sealed interface ListTodoEvent {
    data class DeleteItem(val todo: Todo) : ListTodoEvent
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) : ListTodoEvent
    data class UpdateSortOrder(val todoSortOrder: TodoSortOrder) : ListTodoEvent

    object ClearAll : ListTodoEvent
    object TryAgain : ListTodoEvent
    object RestoreItem : ListTodoEvent
}
