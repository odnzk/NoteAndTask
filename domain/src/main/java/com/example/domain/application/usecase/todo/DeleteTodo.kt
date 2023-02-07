package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteTodo(
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(id: Long) = withContext(dispatcher) {
        todoRepository.delete(id)
    }
}
