package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository

class UpdateTodoCategory(private val todoRepository: TodoRepository) {

    suspend operator fun invoke(todoId: Long, newCategoryId: Long) {
        val item = todoRepository.getById(todoId)
        if (item?.category?.id == newCategoryId) {
            todoRepository.removeCategory(todoId)
        } else {
            todoRepository.updateCategory(todoId, newCategoryId)
        }
    }
}
