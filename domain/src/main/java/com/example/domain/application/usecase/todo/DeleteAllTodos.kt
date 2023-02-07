package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteAllTodos(
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke() = withContext(dispatcher) {
        todoRepository.deleteAll()
    }
}
