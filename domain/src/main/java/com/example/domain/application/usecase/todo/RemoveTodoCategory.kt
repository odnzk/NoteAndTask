package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoveTodoCategory(
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(todoId: Long) = withContext(dispatcher) {
        todoRepository.removeCategory(todoId)
    }
}
