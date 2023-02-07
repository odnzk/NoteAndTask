package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetNoteFlowById(
    private val noteRepository: NoteRepository,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(noteId: Long): Flow<Note?> =
        noteRepository.getNoteFlowById(noteId).flowOn(dispatcher)

}
