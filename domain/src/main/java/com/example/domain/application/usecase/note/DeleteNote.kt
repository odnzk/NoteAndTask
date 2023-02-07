package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteNote(
    private val noteRepository: NoteRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(id: Long) = withContext(dispatcher) { noteRepository.delete(id) }
}
