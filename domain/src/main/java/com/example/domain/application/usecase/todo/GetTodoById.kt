package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.util.exceptions.NotFoundException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetTodoById(
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(todoId: Long): Result<Todo> =
        withContext(dispatcher) {
            todoRepository.getById(todoId)?.let { Result.success(it) } ?: Result.failure(
                NotFoundException()
            )
        }
}
