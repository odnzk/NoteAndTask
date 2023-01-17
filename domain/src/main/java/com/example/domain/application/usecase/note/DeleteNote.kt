package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository

class DeleteNote(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(id: Long) {
        noteRepository.delete(id)
    }
}
