package com.example.domain.application.usecase.both

import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteAll(
    private val noteRepository: NoteRepository,
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(dispatcher) {
        noteRepository.deleteAll()
        todoRepository.deleteAll()
    }
}
