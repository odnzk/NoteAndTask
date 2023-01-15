package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.model.validation.NoteValidator
import com.example.domain.repository.NoteRepository
import com.example.noteapp.ui.util.exceptions.InvalidNoteException

class UpdateNote(
    private val noteRepository: NoteRepository,
    private val noteValidator: NoteValidator
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (noteValidator.isValid(note)) {
            noteRepository.update(note)
        }
    }
}
