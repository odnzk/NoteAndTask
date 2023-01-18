package com.example.data.mapper

import com.example.data.entity.TodoEntity
import com.example.domain.model.Todo

class TodoMapper {
    fun toTodo(todoEntity: TodoEntity): Todo = Todo(
        id = todoEntity.id,
        title = todoEntity.title,
        isCompleted = todoEntity.isCompleted,
        categories = emptyList(),
        deadlineDate = todoEntity.deadlineDate,
    )

    fun from(todo: Todo): TodoEntity {
        return TodoEntity(
            id = todo.id,
            title = todo.title,
            isCompleted = todo.isCompleted,
            categoryId = null,
            deadlineDate = todo.deadlineDate
        )
    }
}
