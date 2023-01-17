package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotes(private val noteRepository: NoteRepository) {

    operator fun invoke(): Flow<List<Note>> {
        return noteRepository.getAll()
    }
}
