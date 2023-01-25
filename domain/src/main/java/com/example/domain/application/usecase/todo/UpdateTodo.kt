package com.example.domain.application.usecase.todo


import com.example.domain.repository.TodoRepository
import com.example.domain.validation.TodoValidator
import com.noteapp.model.Todo

class UpdateTodo(
    private val todoRepository: TodoRepository,
    private val todoValidator: TodoValidator
) {

    suspend operator fun invoke(todo: Todo): Result<Boolean> {
        return todoValidator.isValid(todo).onSuccess { todoRepository.update(todo) }
    }
}
