package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository
import com.example.domain.validation.NoteValidator
import com.example.domain.model.Note

class AddNote(
    private val noteRepository: NoteRepository,
    private val noteValidator: NoteValidator
) {

    suspend operator fun invoke(note: Note): Result<Long> {
        val result: Result<Boolean> = noteValidator.isValid(note)
        return result.exceptionOrNull()?.let { Result.failure(it) }
            ?: Result.success(noteRepository.add(note))
    }
}
