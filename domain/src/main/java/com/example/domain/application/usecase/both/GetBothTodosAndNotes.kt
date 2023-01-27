package com.example.domain.application.usecase.both

import com.example.domain.model.Filter
import com.example.domain.model.FiltersInfo
import com.example.domain.model.NoteItem
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetBothTodosAndNotes
    (
    private val noteRepository: NoteRepository,
    private val todoRepository: TodoRepository
) {

    operator fun invoke(
        filterInfo: FiltersInfo
    ): Flow<List<NoteItem>> {
        // selected category
        val isCategoryEmpty = filterInfo.selectedCategoriesId.isEmpty()
        val notes = if (isCategoryEmpty) noteRepository.getByTitle(filterInfo.searchQuery)
        else noteRepository.getByCategoryId(
            categoryIds = filterInfo.selectedCategoriesId, noteTitle = filterInfo.searchQuery
        )
        val tasks = if (isCategoryEmpty) todoRepository.getByTitle(filterInfo.searchQuery)
        else todoRepository.getByCategoriesId(
            categoryIds = filterInfo.selectedCategoriesId, todoTitle = filterInfo.searchQuery
        )
        return when (filterInfo.filter) {
            // combine because we are waiting for both notes and task
//            Filter.BOTH -> notes.combine(tasks, ::mergeIntoOneList)
            Filter.BOTH -> notes.combine(tasks, ::mergeIntoOneList)
            Filter.NOTES_ONLY -> notes
            Filter.TODO_ONLY -> tasks
        }
    }

    // todo
    private fun mergeIntoOneList(notes: List<NoteItem>, tasks: List<NoteItem>): List<NoteItem> =
        tasks.toMutableList().apply { addAll(notes) }

}
