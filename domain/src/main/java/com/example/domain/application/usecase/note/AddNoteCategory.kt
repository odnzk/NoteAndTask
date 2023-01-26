package com.example.domain.application.usecase.note

import com.example.domain.repository.NoteRepository

class AddNoteCategory(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(noteId: Long, categoryId: Long) {
        val item = noteRepository.getById(noteId)
        if (item?.categories?.map { it.id }?.contains(categoryId) == true) {
            noteRepository.removeCategory(noteId, categoryId)
        } else {
            noteRepository.addCategory(noteId, categoryId)
        }

    }
}
