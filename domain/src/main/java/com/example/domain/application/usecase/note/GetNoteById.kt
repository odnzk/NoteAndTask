package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.noteapp.ui.util.exceptions.NotFoundException

class GetNoteById(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(noteId: Long): Result<Note> {
        return noteRepository.getById(noteId)?.let { Result.success(it) } ?: Result.failure(
            NotFoundException()
        )
    }

}
