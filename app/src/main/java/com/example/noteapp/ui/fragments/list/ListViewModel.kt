package com.example.noteapp.ui.fragments.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.*
import com.example.noteapp.ui.util.PreferenceStorage
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage, // saved state handler
    private val noteUseCases: NoteUseCases,
    private val todoUseCases: TodoUseCases,
    private val categoryUseCases: CategoryUseCases
) :
    ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortOrder = MutableStateFlow(SortOrder.DEFAULT)
    private val filter = MutableStateFlow(Filter.DEFAULT)

    // todo (unite to one state?)
    private var _categoriesList: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())
    val categoryList: StateFlow<List<Category>> = _categoriesList

    private var _noteItemsListState: MutableStateFlow<UiState<List<NoteItem>>> =
        MutableStateFlow(UiState.Loading())
    val noteItemsListState: StateFlow<UiState<List<NoteItem>>> = _noteItemsListState

    private var lastDeletedItem: NoteItem? = null

    init {
        loadData()
    }


    private fun observeSearchAndFiltersStates(): Flow<List<NoteItem>> {
        return combine(
            todoUseCases.getAllTodos(),
            noteUseCases.getAllNotes(searchQuery.value, emptyList(), sortOrder.value) // filter??
        ) { todos, notes ->
            return@combine mutableListOf<NoteItem>().apply {
                addAll(todos)
                addAll(notes)
            }
        }
    }
//        return combine(searchQuery, sortOrder, filter) { searchQuery, sortOrder, filter ->
//            val notes = noteRepository.getAll(searchQuery, sortOrder, filter)
//            val todoitem = todoRepository.getAll()
//
//            mutableListOf<NoteItem>().apply {
//                // todoitem
//                addAll(notes.first())
//                addAll(todoitem.first())
//            }
//        } }

    fun loadData() {
        // todo check hierarchy
        viewModelScope.async {
            _noteItemsListState.value = UiState.Loading()
            observeSearchAndFiltersStates().collect {
                _noteItemsListState.value = UiState.Success(it)
            }
        }
        viewModelScope.async {
            categoryUseCases.getAllCategories().collectLatest {
                _categoriesList.value = it
            }
        }
    }


    fun onEvent(event: ListFragmentEvent) {
        viewModelScope.launch {
            when (event) {
                is ListFragmentEvent.AddItem -> {
                    when (val noteItem = event.noteItem) {
                        is Note -> noteUseCases.addNote(noteItem)
                        is Todo -> todoUseCases.addTodo(noteItem)
                    }
                }
                is ListFragmentEvent.DeleteItem -> {
                    when (val noteItem = event.noteItem) {
                        is Note -> noteUseCases.deleteNote(noteItem)
                        is Todo -> todoUseCases.deleteTodo(noteItem)
                    }
                    lastDeletedItem = event.noteItem
                }
                is ListFragmentEvent.RestoreItem -> {
                    lastDeletedItem?.let { noteItem ->
                        when (noteItem) {
                            is Note -> noteUseCases.addNote(noteItem)
                            is Todo -> todoUseCases.addTodo(noteItem)
                        }
                    }
                }
                is ListFragmentEvent.UpdateSortOrder -> sortOrder.value = event.sortOrder
                is ListFragmentEvent.UpdateFilter -> filter.value = event.filter
                is ListFragmentEvent.ClearAll -> {
                    noteUseCases.deleteAllNotes()
                    todoUseCases.deleteAllTodo()
                }
                is ListFragmentEvent.UpdateTodoCompletedStatus -> {
                    todoUseCases.updateIsCompleted(event.todoId, event.isCompleted)
                }
            }
        }
    }

}
