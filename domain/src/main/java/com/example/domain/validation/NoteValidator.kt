package com.example.domain.validation

import com.example.domain.model.Note
import com.example.noteapp.ui.util.exceptions.InvalidNoteException

class NoteValidator {

    fun isValid(note: Note): Result<Boolean> {
        return if (note.title.isBlank() || note.title.length !in MIN_LENGTH..MAX_LENGTH) {
            Result.failure(InvalidNoteException(Field.TITLE))
        } else if (note.content.isBlank()) {
            Result.failure(InvalidNoteException(Field.CONTENT))
        } else {
            Result.success(true)
        }
    }

    companion object {
        const val MAX_LENGTH = 40
        const val MIN_LENGTH = 2
    }
}
