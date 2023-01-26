package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository
import com.example.domain.validation.NoteValidator
import com.example.domain.model.Note

class UpdateNote(
    private val noteRepository: NoteRepository,
    private val noteValidator: NoteValidator
) {

    suspend operator fun invoke(note: Note): Result<Boolean> {
        return noteValidator.isValid(note).onSuccess { noteRepository.update(note) }
    }

}
