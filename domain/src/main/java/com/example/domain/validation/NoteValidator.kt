package com.example.domain.validation

import com.example.noteapp.ui.util.exceptions.InvalidNoteException
import com.example.domain.model.Note

class NoteValidator {

    fun isValid(note: Note): Result<Boolean> {
        return if (note.title.isBlank()) {
            Result.failure(InvalidNoteException(Field.TITLE))
        } else if (note.content.isBlank()) {
            Result.failure(InvalidNoteException(Field.CONTENT))
        } else {
            Result.success(true)
        }
    }
}
