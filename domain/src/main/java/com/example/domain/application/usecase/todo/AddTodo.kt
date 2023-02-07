package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.domain.validation.TodoValidator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddTodo(
    private val todoRepository: TodoRepository,
    private val todoValidator: TodoValidator,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(todo: Todo): Result<Long> = withContext(dispatcher) {
        val result = todoValidator.isValid(todo)
        result.exceptionOrNull()?.let { Result.failure(it) }
            ?: Result.success(todoRepository.add(todo))
    }
}
