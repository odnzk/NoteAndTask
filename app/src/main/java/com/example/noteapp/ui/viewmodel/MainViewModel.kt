package com.example.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import com.example.domain.util.Filter
import com.example.domain.util.SortOrder
import com.example.noteapp.ui.util.PreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NoteItemEvent {
    data class AddItem(val noteItem: NoteItem) : NoteItemEvent
    data class DeleteItem(val noteItem: NoteItem) : NoteItemEvent
    data class UpdateNoteItem(val noteItem: NoteItem) : NoteItemEvent
    object RestoreItem : NoteItemEvent
    class UpdateSortOrder(val sortOrder: SortOrder) : NoteItemEvent
    class UpdateFilter(val filter: Filter) : NoteItemEvent
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository,
    private val todoRepository: TodoRepository,
    private val preferenceStorage: PreferenceStorage
) :
    ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortOrder = MutableStateFlow(SortOrder.BY_CATEGORY_PRIORITY)
    private val filter = MutableStateFlow(Filter.DEFAULT)

    private val _noteItemsListState: MutableStateFlow<ListState> =
        MutableStateFlow(ListState.Loading)
    val noteItemsListState: StateFlow<ListState> = _noteItemsListState

    init {
        loadData()
    }


    private fun combineTodosWithNotes(): Flow<List<NoteItem>> {
        return combine(searchQuery, sortOrder, filter) { searchQuery, sortOrder, filter ->
            Triple(searchQuery, sortOrder, filter)
        }.flatMapLatest { (searchQuery, sortOrder, filter) ->
            noteRepository.getAll(
                searchQuery,
                sortOrder,
                filter
            )
        }
    }

    fun loadData() {
        // todo some loading
        viewModelScope.launch {

        }
//        noteItemsListState = ListState.Success(noteItemFlow)
    }


    fun onEvent(event: NoteItemEvent) {
        when (event) {
            is NoteItemEvent.AddItem -> {
                viewModelScope.launch {
                    if (event.noteItem is Note) {
                        noteRepository.add(event.noteItem)
                    }
                }
            }
            is NoteItemEvent.DeleteItem -> {
                viewModelScope.launch {
                    if (event.noteItem is Note) {
                        noteRepository.delete(event.noteItem)
                    }
                }
            }
            is NoteItemEvent.RestoreItem -> {

            }
            is NoteItemEvent.UpdateSortOrder -> {

            }
            is NoteItemEvent.UpdateFilter -> {

            }
            is NoteItemEvent.UpdateNoteItem -> TODO()
        }
    }


}
