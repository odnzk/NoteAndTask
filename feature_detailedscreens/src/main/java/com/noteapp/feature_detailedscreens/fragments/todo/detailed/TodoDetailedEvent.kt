package com.noteapp.feature_detailedscreens.fragments.todo.detailed

import com.noteapp.model.Todo

sealed interface TodoDetailedEvent {
    data class UpdateTodo(val todo: Todo) : TodoDetailedEvent

    object TryLoadingTodoAgain : TodoDetailedEvent
    object DeleteTodo : TodoDetailedEvent
}
