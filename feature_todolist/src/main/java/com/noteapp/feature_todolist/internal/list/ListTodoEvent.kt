package com.noteapp.feature_todolist.internal.list

import com.noteapp.model.Todo
import com.noteapp.model.TodoSortOrder

internal sealed interface ListTodoEvent {
    data class DeleteItem(val todo: Todo) : ListTodoEvent
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) : ListTodoEvent
    data class UpdateSortOrder(val todoSortOrder: TodoSortOrder) : ListTodoEvent

    object ClearAll : ListTodoEvent
    object TryAgain : ListTodoEvent
    object RestoreItem : ListTodoEvent
}
