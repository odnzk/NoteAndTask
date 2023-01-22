package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.domain.validation.NoteValidator

class UpdateNote(
    private val noteRepository: NoteRepository,
    private val noteValidator: NoteValidator
) {

    suspend operator fun invoke(note: Note): Result<Boolean> {
        return noteValidator.isValid(note).onSuccess { noteRepository.update(note) }
    }

}
