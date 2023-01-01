package com.example.domain.model.validation

import com.example.domain.model.Note
import com.example.domain.util.exceptions.Field
import com.example.noteapp.ui.util.exceptions.InvalidNoteException

class NoteValidator {

    @Throws(InvalidNoteException::class)
    fun isValid(note: Note): Boolean {
        if (note.title.isBlank()) {
            throw InvalidNoteException(Field.TITLE)
        } else if (note.content.isBlank()) {
            throw InvalidNoteException(Field.CONTENT)
        }
        return true
    }
}
