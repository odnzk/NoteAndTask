package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository

class DeleteTodo(private val todoRepository: TodoRepository) {

    suspend operator fun invoke(todo: Todo) {
        todoRepository.delete(todo)
    }
}
