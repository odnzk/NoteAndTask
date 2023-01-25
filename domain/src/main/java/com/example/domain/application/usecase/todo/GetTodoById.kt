package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.util.exceptions.NotFoundException
import com.noteapp.model.Todo

class GetTodoById(private val todoRepository: TodoRepository) {

    suspend operator fun invoke(todoId: Long): Result<Todo> =
        todoRepository.getById(todoId)?.let { Result.success(it) } ?: Result.failure(
            NotFoundException()
        )
}
