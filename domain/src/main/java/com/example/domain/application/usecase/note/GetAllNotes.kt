package com.example.domain.application.usecase.note

import com.example.domain.model.Note
import com.example.domain.model.NoteSort
import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllNotes(private val noteRepository: NoteRepository) {

    operator fun invoke(noteSort: NoteSort = NoteSort.DEFAULT): Flow<List<Note>> {
        return when (noteSort) {
            NoteSort.DEFAULT -> noteRepository.getAll()
            NoteSort.BY_DATE -> noteRepository.getAll()
                .map { it.sortedBy { note -> note.date } }
            NoteSort.BY_ALPHABET -> noteRepository.getAll()
                .map { it.sortedBy { note -> note.title } }
        }
    }
}
