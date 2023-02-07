package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.domain.validation.NoteValidator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateNote(
    private val noteRepository: NoteRepository,
    private val noteValidator: NoteValidator,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(note: Note): Result<Boolean> = withContext(dispatcher) {
        noteValidator.isValid(note).onSuccess { noteRepository.update(note) }
    }

}
