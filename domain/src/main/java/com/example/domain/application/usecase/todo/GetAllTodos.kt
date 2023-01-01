package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetAllTodos(private val todoRepository: TodoRepository) {

    // todo
    operator fun invoke(): Flow<List<Todo>> = todoRepository.getAll()
}
