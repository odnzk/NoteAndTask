package com.example.domain.validation

import com.example.domain.model.Note
import com.example.noteapp.ui.util.exceptions.InvalidNoteException

class NoteValidator {

    fun hasException(note: Note): Exception? {
        return if (note.title.isBlank() || note.title.length !in MIN_LENGTH..MAX_LENGTH) {
            InvalidNoteException(Field.TITLE)
        } else if (note.content.isBlank()) {
            InvalidNoteException(Field.CONTENT)
        } else null
    }

    companion object {
        const val MAX_LENGTH = 40
        const val MIN_LENGTH = 2
    }
}
