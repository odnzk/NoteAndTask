package com.example.domain.application.usecase.both

import com.example.domain.model.FiltersInfo
import com.example.domain.model.NoteItem
import com.example.domain.model.NoteItemFilter
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import com.example.domain.util.sorting.DefaultSorter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetBothTodosAndNotes
    (
    private val noteRepository: NoteRepository,
    private val todoRepository: TodoRepository,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        filterInfo: FiltersInfo
    ): Flow<List<NoteItem>> {
        val isSelectedCategoryEmpty = filterInfo.selectedCategoriesId.isEmpty()
        val notes: Flow<List<NoteItem>>
        var tasks: Flow<List<NoteItem>>
        if (isSelectedCategoryEmpty) {
            notes = noteRepository.getByTitle(filterInfo.searchQuery)
            tasks = todoRepository.getByTitle(filterInfo.searchQuery)
        } else {
            notes = noteRepository.getByCategoryId(
                filterInfo.selectedCategoriesId,
                filterInfo.searchQuery
            )
            tasks = todoRepository.getByCategoriesId(
                filterInfo.selectedCategoriesId,
                filterInfo.searchQuery
            )
        }
        tasks = tasks.map { DefaultSorter.sort(it) }
        return when (filterInfo.filter) {
            NoteItemFilter.BOTH -> notes.combine(tasks, ::mergeIntoOneList)
            NoteItemFilter.NOTES_ONLY -> notes
            NoteItemFilter.TODO_ONLY -> tasks
        }.flowOn(dispatcher)
    }

    private fun mergeIntoOneList(notes: List<NoteItem>, tasks: List<NoteItem>): List<NoteItem> =
        tasks.toMutableList().apply { addAll(notes) }

}
