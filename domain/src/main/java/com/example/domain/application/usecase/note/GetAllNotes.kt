package com.example.domain.application.usecase.note

import com.example.domain.model.Category
import com.example.domain.model.Filter
import com.example.domain.model.Note
import com.example.domain.model.SortOrder
import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotes(private val noteRepository: NoteRepository) {

    // todo
    operator fun invoke(
        searhQuery: String? = "",
        selectedCategories: List<Category> = emptyList(),
        sortOrder: SortOrder = SortOrder.DEFAULT
    ): Flow<List<Note>> =
        noteRepository.getAll("", sortOrder, Filter.DEFAULT)
}
