package com.example.domain.application.usecase.both

import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository

class DeleteAll(
    private val noteRepository: NoteRepository,
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke() {
        noteRepository.deleteAll()
        todoRepository.deleteAll()
    }
}
