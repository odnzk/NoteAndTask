package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import com.example.domain.validation.TodoValidator
import com.noteapp.model.Todo

class AddTodo(
    private val todoRepository: TodoRepository,
    private val todoValidator: TodoValidator
) {

    suspend operator fun invoke(todo: Todo): Result<Long> {
        val result = todoValidator.isValid(todo)
        return result.exceptionOrNull()?.let { Result.failure(it) }
            ?: Result.success(todoRepository.add(todo))
    }
}
