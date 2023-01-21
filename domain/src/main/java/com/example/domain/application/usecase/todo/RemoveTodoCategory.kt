package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository

class RemoveTodoCategory(private val todoRepository: TodoRepository) {

    suspend operator fun invoke(todoId: Long) {
        todoRepository.removeCategory(todoId)
    }
}
