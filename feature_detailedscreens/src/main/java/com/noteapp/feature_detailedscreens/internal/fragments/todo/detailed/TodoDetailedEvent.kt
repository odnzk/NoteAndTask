package com.noteapp.feature_detailedscreens.internal.fragments.todo.detailed

import com.noteapp.model.Todo

internal sealed interface TodoDetailedEvent {
    data class UpdateTodo(val todo: Todo) : TodoDetailedEvent

    object TryLoadingTodoAgain : TodoDetailedEvent
    object DeleteTodo : TodoDetailedEvent
}
