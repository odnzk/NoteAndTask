package com.example.noteapp.ui.fragments.events

sealed interface ListTodoEvent {
    object ClearAll : ListTodoEvent
    object TryAgain : ListTodoEvent
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) : ListTodoEvent
}
