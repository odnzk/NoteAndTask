package com.example.noteapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.domain.model.Todo
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
    object ClearAll : NoteItemEvent
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

    private var _noteItemsListState: MutableStateFlow<ListState<List<NoteItem>>> =
        MutableStateFlow(ListState.Loading())
    val noteItemsListState: StateFlow<ListState<List<NoteItem>>> = _noteItemsListState

    init {
        Log.d("TAGTAG", "${this.javaClass}: loading")
        loadData()
    }


    private fun observeSearchAndFiltersStates(): Flow<List<NoteItem>> {
        return combine(searchQuery, sortOrder, filter) { searchQuery, sortOrder, filter ->
            val notes = noteRepository.getAll(searchQuery, sortOrder, filter)
            val todo = todoRepository.getAll()

            mutableListOf<NoteItem>().apply {
                // todo
                addAll(notes.first())
                addAll(todo.first())
            }
        }
    }

    fun loadData() {
        Log.d("TAGTAG", "${this.javaClass}: loading")
        viewModelScope.launch {
            _noteItemsListState.emit(ListState.Loading())
            observeSearchAndFiltersStates().collectLatest {
                Log.d("TAGTAG", "${this.javaClass}: success")
                _noteItemsListState.emit(ListState.Success(it))
            }
        }
    }


    fun onEvent(event: NoteItemEvent) {
        when (event) {
            is NoteItemEvent.AddItem -> {
                viewModelScope.launch {
                    when (val noteItem = event.noteItem) {
                        is Note -> noteRepository.add(noteItem)
                        is Todo -> todoRepository.add(noteItem)
                    }
                }
            }
            is NoteItemEvent.DeleteItem -> {
                viewModelScope.launch {
                    when (val noteItem = event.noteItem) {
                        is Note -> noteRepository.delete(noteItem)
                        is Todo -> todoRepository.delete(noteItem)
                    }
                }
            }
            is NoteItemEvent.RestoreItem -> {

            }
            is NoteItemEvent.UpdateSortOrder -> {

            }
            is NoteItemEvent.UpdateFilter -> {

            }
            is NoteItemEvent.UpdateNoteItem -> {

            }
            is NoteItemEvent.ClearAll -> {

            }
        }
    }


}
