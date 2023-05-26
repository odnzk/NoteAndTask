package com.noteapp.feature_todolist.util.mappers

import com.example.domain.model.TodoFilters
import com.noteapp.feature_todolist.model.UiTodoFilters

internal fun UiTodoFilters.toTodoFilters() =
    TodoFilters(emptySet(), period = period, additionalConditions = additionalConditions)
