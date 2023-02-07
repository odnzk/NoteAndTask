package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateTodoCategory(
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(todoId: Long, newCategoryId: Long) = withContext(dispatcher) {
        val item = todoRepository.getById(todoId)
        if (item?.category?.id == newCategoryId) {
            todoRepository.removeCategory(todoId)
        } else {
            todoRepository.updateCategory(todoId, newCategoryId)
        }
    }
}
