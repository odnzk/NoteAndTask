package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.model.validation.TodoValidator
import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.util.exceptions.InvalidTodoException

class AddTodo(private val todoRepository: TodoRepository) {

    @Throws(InvalidTodoException::class)
    suspend operator fun invoke(todo: Todo) {
        if (TodoValidator().isValid(todo)) {
            todoRepository.add(todo)
        }
    }
}
