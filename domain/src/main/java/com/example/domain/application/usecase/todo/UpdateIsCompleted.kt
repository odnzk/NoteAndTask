package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateIsCompleted(
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(todoId: Long, isCompleted: Boolean) = withContext(dispatcher){
        todoRepository.updateCompletedStatus(todoId, isCompleted)
    }
}
