package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.domain.validation.TodoValidator

class UpdateTodo(
    private val todoRepository: TodoRepository,
    private val todoValidator: TodoValidator
) {

    suspend operator fun invoke(todo: Todo): Result<Boolean> {
        return todoValidator.isValid(todo).onSuccess { todoRepository.update(todo) }
    }
}
