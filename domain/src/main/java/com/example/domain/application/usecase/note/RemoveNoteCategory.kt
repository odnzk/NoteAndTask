package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository

class RemoveNoteCategory(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(noteId: Long, categoryId: Long) {
        noteRepository.removeCategory(noteId, categoryId)
    }
}
