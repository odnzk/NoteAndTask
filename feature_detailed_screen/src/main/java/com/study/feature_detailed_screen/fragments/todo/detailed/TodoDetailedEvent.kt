package com.study.feature_detailed_screen.fragments.todo.detailed

import com.example.domain.model.Todo

internal sealed interface TodoDetailedEvent {
    data class UpdateTodo(val todo: Todo) : TodoDetailedEvent
    object Reload : TodoDetailedEvent
    object DeleteTodo : TodoDetailedEvent
}
