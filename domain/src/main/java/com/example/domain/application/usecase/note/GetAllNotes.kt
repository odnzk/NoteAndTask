package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.model.NoteSortOrder
import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetAllNotes(
    private val noteRepository: NoteRepository,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(noteSort: NoteSortOrder = NoteSortOrder.DEFAULT): Flow<List<Note>> =
        when (noteSort) {
            NoteSortOrder.DEFAULT -> noteRepository.getAll()
            NoteSortOrder.BY_DATE -> noteRepository.getAll()
                .map { it.sortedBy { note -> note.date } }
            NoteSortOrder.BY_ALPHABET -> noteRepository.getAll()
                .map { it.sortedBy { note -> note.title } }
        }.flowOn(dispatcher)

}
