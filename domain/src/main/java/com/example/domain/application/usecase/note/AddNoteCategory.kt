package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository

class AddNoteCategory(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(noteId: Long, categoryId: Long) {
        noteRepository.addCategory(noteId, categoryId)
    }
}
