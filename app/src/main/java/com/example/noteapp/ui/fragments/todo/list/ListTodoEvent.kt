package com.example.noteapp.ui.fragments.todo.list

sealed interface ListTodoEvent {
    object ClearAll : ListTodoEvent
    object TryAgain : ListTodoEvent
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) : ListTodoEvent
}
