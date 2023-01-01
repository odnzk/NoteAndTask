package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.util.exceptions.NotFoundException

class GetTodoById(private val todoRepository: TodoRepository) {

    // todo
    @Throws(NotFoundException::class)
    suspend operator fun invoke(todoId: Long): Todo =
        todoRepository.getById(todoId) ?: throw NotFoundException()
}
