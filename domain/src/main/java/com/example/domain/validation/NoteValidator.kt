package com.example.domain.validation

import com.example.domain.model.Note
import com.example.domain.util.Field
import com.example.noteapp.ui.util.exceptions.InvalidNoteException

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
