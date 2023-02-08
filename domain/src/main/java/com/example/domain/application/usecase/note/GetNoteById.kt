package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.domain.util.exceptions.NotFoundException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetNoteById(
    private val noteRepository: NoteRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(noteId: Long): Result<Note> = withContext(dispatcher) {
        noteRepository.getById(noteId)?.let { note -> Result.success(note) }
            ?: Result.failure(NotFoundException())
    }

}
