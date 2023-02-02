package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.util.exceptions.InvalidTodoException

class UpdateIsCompleted(private val todoRepository: TodoRepository) {

    suspend operator fun invoke(todoId: Long, isCompleted: Boolean) {
        todoRepository.updateCompletedStatus(todoId, isCompleted)
    }
}
