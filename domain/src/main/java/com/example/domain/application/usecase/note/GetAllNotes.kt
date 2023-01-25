package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository
import com.noteapp.model.Note
import com.noteapp.model.NoteSortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllNotes(private val noteRepository: NoteRepository) {

    operator fun invoke(noteSort: NoteSortOrder = NoteSortOrder.DEFAULT): Flow<List<Note>> =
        when (noteSort) {
            NoteSortOrder.DEFAULT -> noteRepository.getAll()
            NoteSortOrder.BY_DATE -> noteRepository.getAll()
                .map { it.sortedBy { note -> note.date } }
            NoteSortOrder.BY_ALPHABET -> noteRepository.getAll()
                .map { it.sortedBy { note -> note.title } }
        }

}
