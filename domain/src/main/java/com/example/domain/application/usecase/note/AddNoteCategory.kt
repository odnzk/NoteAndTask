package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddNoteCategory(
    private val noteRepository: NoteRepository, private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(noteId: Long, categoryId: Long) = withContext(dispatcher) {
        val item = noteRepository.getById(noteId)
        if (item?.categories?.map { it.id }?.contains(categoryId) == true) {
            noteRepository.removeCategory(noteId, categoryId)
        } else {
            noteRepository.addCategory(noteId, categoryId)
        }
    }
}
