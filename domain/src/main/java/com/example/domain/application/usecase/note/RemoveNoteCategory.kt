package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoveNoteCategory(
    private val noteRepository: NoteRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(noteId: Long, categoryId: Long) = withContext(dispatcher) {
        noteRepository.removeCategory(noteId, categoryId)
    }
}
