package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.domain.validation.NoteValidator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddNote(
    private val noteRepository: NoteRepository,
    private val noteValidator: NoteValidator,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(note: Note): Result<Long> = withContext(dispatcher) {
        noteValidator.hasException(note)?.let { Result.failure(it) } ?: noteRepository.add(note)
    }
}
