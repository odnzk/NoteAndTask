package com.example.data.mapper

import com.example.data.entity.TodoEntity
import com.example.data.tuples.TodoWithCategoryTuple
import com.noteapp.model.Todo

internal fun Todo.toEntity() = TodoEntity(
    id = id,
    title = title,
    isCompleted = isCompleted,
    categoryId = category?.id,
    deadlineDate = deadlineDate
)

internal fun TodoWithCategoryTuple.toTodo() = Todo(
    id = todo.id,
    title = todo.title,
    isCompleted = todo.isCompleted,
    category = category?.toCategory(),
    deadlineDate = todo.deadlineDate,
)
