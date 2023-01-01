package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.noteapp.ui.util.exceptions.NotFoundException

class GetNoteById(private val noteRepository: NoteRepository) {

    @Throws(NotFoundException::class)
    suspend operator fun invoke(noteId: Long): Note =
        noteRepository.getById(noteId) ?: throw NotFoundException()
}
