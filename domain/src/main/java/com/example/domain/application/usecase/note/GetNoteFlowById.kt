package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNoteFlowById(private val noteRepository: NoteRepository) {

    operator fun invoke(noteId: Long): Flow<Note?> {
        return noteRepository.getNoteFlowById(noteId)
    }
}
