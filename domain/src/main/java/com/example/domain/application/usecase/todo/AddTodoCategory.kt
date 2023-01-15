package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository

class AddTodoCategory(private val todoRepository: TodoRepository) {

    suspend operator fun invoke(todoId: Long, categoryId: Long) {
        todoRepository.addCategory(todoId, categoryId)
    }
}
