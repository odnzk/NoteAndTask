package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository

class DeleteAllNotes(private val noteRepository: NoteRepository) {

    suspend operator fun invoke() {
        noteRepository.deleteAll()
    }
}
