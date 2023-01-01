package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository

class DeleteAllTodos(private val todoRepository: TodoRepository) {

    suspend operator fun invoke() {
        todoRepository.deleteAll()
    }
}
