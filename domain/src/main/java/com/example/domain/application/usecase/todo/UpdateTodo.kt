package com.example.domain.application.usecase.todo


import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.domain.validation.TodoValidator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateTodo(
    private val todoRepository: TodoRepository,
    private val todoValidator: TodoValidator,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(todo: Todo): Result<Long> = withContext(dispatcher) {
        todoValidator.hasException(todo)?.let { Result.failure(it) } ?: todoRepository.update(todo)
    }
}
