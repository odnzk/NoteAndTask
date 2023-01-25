package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository
import com.example.noteapp.ui.util.exceptions.NotFoundException
import com.noteapp.model.Note

class GetNoteById(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(noteId: Long): Result<Note> {
        return noteRepository.getById(noteId)?.let { note -> Result.success(note) }
            ?: Result.failure(
                NotFoundException()
            )
    }

}
