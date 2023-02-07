package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetTodoFlowById(
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(todoId: Long): Flow<Todo?> =
        todoRepository.getTodoFlowById(todoId).flowOn(dispatcher)

}
