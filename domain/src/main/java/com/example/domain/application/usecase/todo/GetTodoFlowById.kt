package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetTodoFlowById(private val todoRepository: TodoRepository) {

    operator fun invoke(todoId: Long): Flow<Todo?> = todoRepository.getTodoFlowById(todoId)
//        todoRepository.getById(todoId)?.let { Result.success(it) } ?: Result.failure(
//            NotFoundException()
//        )

}
