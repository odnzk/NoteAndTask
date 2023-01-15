package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.model.validation.TodoValidator
import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.util.exceptions.InvalidTodoException

class UpdateTodo(
    private val todoRepository: TodoRepository,
    private val todoValidator: TodoValidator
) {

    @Throws(InvalidTodoException::class)
    suspend operator fun invoke(todo: Todo) {
        if (todoValidator.isValid(todo)) {
            todoRepository.update(todo)
        }
    }
}
