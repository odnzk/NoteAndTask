package com.example.data.mapper

import com.example.data.entity.TodoEntity
import com.example.domain.model.Todo

fun Todo.toEntity() = TodoEntity(
    id = id,
    title = title,
    isCompleted = isCompleted,
    categoryId = null,
    deadlineDate = deadlineDate
)

fun TodoEntity.toTodo() = Todo(
    id = id,
    title = title,
    isCompleted = isCompleted,
    categories = emptyList(),
    deadlineDate = deadlineDate,
)
