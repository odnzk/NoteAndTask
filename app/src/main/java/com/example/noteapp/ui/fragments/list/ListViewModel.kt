package com.example.noteapp.ui.fragments.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.both.UnitedUseCases
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.*
import com.example.noteapp.ui.util.PreferenceStorage
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage, // saved state handler
    private val noteUseCases: NoteUseCases,
    private val todoUseCases: TodoUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val unitedUseCases: UnitedUseCases
) :
    ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val filter = MutableStateFlow(Filter.BOTH)

    // todo (unite to one state?)
    private var _categoriesList: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())
    val categoryList = _categoriesList.asStateFlow()

    private var _noteItemsListState: MutableStateFlow<UiState<List<NoteItem>>> =
        MutableStateFlow(UiState.Loading())
    val noteItemsListState = _noteItemsListState.asStateFlow()

    private var lastDeletedItem: NoteItem? = null

    init {
        loadData()
    }

    fun loadData() =
        viewModelScope.launch {
            // subscribe to notes and tasks list
            launch {
//                unitedUseCases
//                    .getBothTodosAndNotes(searchQuery.value, filter.value)
//                    .distinctUntilChanged()
//                    .collectLatest {
//                        Log.d("hello", "updating list")
//                        _noteItemsListState.value = UiState.Success(it)
//                    }
            }
            combine(searchQuery, unitedUseCases.getBothTodosAndNotes(searchQuery.value, filter.value)){searchQuery, notesAndTasks->
            }.collectLatest {
                Log.d("hello", "collecting...")
            }
//            searchQuery.collectLatest {
//                _noteItemsListState.value = UiState.Loading()
//
//            }

            // subscribe to categories
            categoryUseCases
                .getAllCategories()
                .distinctUntilChanged()
                .collectLatest {
                    _categoriesList.value = it
                }
        }

    fun onEvent(event: ListFragmentEvent) {
        viewModelScope.launch {
            when (event) {
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
                is ListFragmentEvent.UpdateFilter -> filter.value = event.filter
                is ListFragmentEvent.ClearAll -> unitedUseCases.deleteAll()
                is ListFragmentEvent.UpdateTodoCompletedStatus -> {
                    todoUseCases.updateIsCompleted(event.todoId, event.isCompleted)
                }
                is ListFragmentEvent.UpdateSearchQuery -> {
                    searchQuery.value = event.query
                    Log.d("hello", "changed in view model to ${searchQuery.value}")
                }
            }
        }
    }

}
