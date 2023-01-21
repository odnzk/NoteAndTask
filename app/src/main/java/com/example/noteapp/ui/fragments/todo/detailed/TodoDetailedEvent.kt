package com.example.noteapp.ui.fragments.todo.detailed

import com.example.domain.model.Todo

sealed interface TodoDetailedEvent {
    data class UpdateTodo(val todo: Todo) : TodoDetailedEvent

    object TryLoadingTodoAgain : TodoDetailedEvent
    object DeleteTodo : TodoDetailedEvent
}
