package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.validation.NoteValidator
import com.example.domain.repository.NoteRepository

class UpdateNote(
    private val noteRepository: NoteRepository,
    private val noteValidator: NoteValidator
) {

    suspend operator fun invoke(note: Note): Result<Boolean> {
        val result: Result<Boolean> = noteValidator.isValid(note)
        if (result.isSuccess) {
            noteRepository.update(note)
        }
        return result
    }

}
