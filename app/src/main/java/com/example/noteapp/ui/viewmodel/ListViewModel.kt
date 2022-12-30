package com.example.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Category
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.domain.model.Todo
import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import com.example.domain.util.Filter
import com.example.domain.util.SortOrder
import com.example.noteapp.ui.fragments.events.ListFragmentEvent
import com.example.noteapp.ui.util.PreferenceStorage
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository,
    private val todoRepository: TodoRepository,
    private val preferenceStorage: PreferenceStorage
) :
    ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortOrder = MutableStateFlow(SortOrder.BY_CATEGORY_PRIORITY)
    private val filter = MutableStateFlow(Filter.DEFAULT)

    // todo (unite to one state?)
    private var _categoriesList: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())
    val categoryList: StateFlow<List<Category>> = MutableStateFlow(emptyList())

    private var _noteItemsListState: MutableStateFlow<UiState<List<NoteItem>>> =
        MutableStateFlow(UiState.Loading())
    val noteItemsListState: StateFlow<UiState<List<NoteItem>>> = _noteItemsListState

    init {
        loadData()
    }


    private fun observeSearchAndFiltersStates(): Flow<List<NoteItem>> {
        return combine(
            todoRepository.getAll(),
            noteRepository.getAll(searchQuery.value, sortOrder.value, filter.value)
        ) { todos, notes ->
            return@combine mutableListOf<NoteItem>().apply {
                addAll(todos)
                addAll(notes)
            }
        }
    }
//        return combine(searchQuery, sortOrder, filter) { searchQuery, sortOrder, filter ->
//            val notes = noteRepository.getAll(searchQuery, sortOrder, filter)
//            val todo = todoRepository.getAll()
//
//            mutableListOf<NoteItem>().apply {
//                // todo
//                addAll(notes.first())
//                addAll(todo.first())
//            }
//        } }

    fun loadData() {
        viewModelScope.launch {
            _noteItemsListState.value = UiState.Loading()
            observeSearchAndFiltersStates().collect {
                _noteItemsListState.value = UiState.Success(it)
            }
            categoryRepository.getAll().collect {
                _categoriesList.value = it
            }
        }
    }


    fun onEvent(event: ListFragmentEvent) {
        viewModelScope.launch {
            when (event) {
                is ListFragmentEvent.AddItem -> {
                    when (val noteItem = event.noteItem) {
                        is Note -> noteRepository.add(noteItem)
                        is Todo -> todoRepository.add(noteItem)
                    }
                }
                is ListFragmentEvent.DeleteItem -> {
                    when (val noteItem = event.noteItem) {
                        is Note -> noteRepository.delete(noteItem)
                        is Todo -> todoRepository.delete(noteItem)
                    }
                }
                is ListFragmentEvent.RestoreItem -> {

                }
                is ListFragmentEvent.UpdateSortOrder -> {

                }
                is ListFragmentEvent.UpdateFilter -> {

                }
                is ListFragmentEvent.ClearAll -> {

                }
                is ListFragmentEvent.UpdateTodoCompletedStatus -> {
                    todoRepository.updateCompletedStatus(event.todoId, event.isCompleted)
                }
            }
        }
    }

}
