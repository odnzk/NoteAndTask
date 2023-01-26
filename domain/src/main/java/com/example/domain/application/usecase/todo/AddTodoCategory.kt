package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository

class AddTodoCategory(private val todoRepository: TodoRepository) {

    suspend operator fun invoke(todoId: Long, currentCategoryId: Long, newCategoryId: Long) {
        if (currentCategoryId == newCategoryId) {
            todoRepository.removeCategory(currentCategoryId)
        } else {
            todoRepository.updateCategory(todoId, currentCategoryId)
        }
    }
}
