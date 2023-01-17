package com.example.noteapp.ui.fragments.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.both.UnitedUseCases
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.domain.model.Todo
import com.example.noteapp.ui.util.PreferenceStorage
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private var _listState: MutableStateFlow<UiState<ListFragmentState>> =
        MutableStateFlow(UiState.Loading())
    val listState = _listState.asStateFlow()

    private var lastDeletedItem: NoteItem? = null

    init {
        loadData()
    }

    private fun loadData() =
        viewModelScope.launch {
            // subscribe to noteItems
            unitedUseCases
                .getBothTodosAndNotes()
                .distinctUntilChanged()
                .collectLatest { noteItems ->
                    // if state = Success -> update existing state
                    listState.value.data?.let {
                        _listState.value = UiState.Success(it.copy(noteItems = noteItems))
                    } ?: run {
                        // if state != Success -> create new state
                        _listState.value = UiState.Success(ListFragmentState(noteItems = noteItems))
                    }
                }

            // subscribe to categories
            categoryUseCases
                .getAllCategories()
                .distinctUntilChanged()
                .collectLatest { categories ->
                    // if state = Success -> update existing state
                    listState.value.data?.let {
                        _listState.value = UiState.Success(it.copy(categories = categories))
                    } ?: run {
                        // if state != Success -> create new state
                        _listState.value =
                            UiState.Success(ListFragmentState(categories = categories))
                    }
                }
        }

    fun onEvent(event: ListFragmentEvent) {
        viewModelScope.launch {
            when (event) {
                is ListFragmentEvent.DeleteItem -> {
                    when (val noteItem = event.noteItem) {
                        is Note -> noteUseCases.deleteNote(noteItem)
                        is Todo -> todoUseCases.deleteTodo(noteItem.id)
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
                is ListFragmentEvent.UpdateFilter -> listState.value.data?.let {
                    _listState.value = UiState.Success(it.copy(filter = event.filter))
                }
                is ListFragmentEvent.ClearAll -> unitedUseCases.deleteAll()
                is ListFragmentEvent.UpdateTodoCompletedStatus -> {
                    todoUseCases.updateIsCompleted(event.todoId, event.isCompleted)
                }
                is ListFragmentEvent.UpdateSearchQuery -> listState.value.data?.let {
                    _listState.value = UiState.Success(it.copy(searchQuery = event.query))
                }
                ListFragmentEvent.ReloadData -> TODO()
                is ListFragmentEvent.UpdateSelectedCategoryId -> TODO()
            }
        }
    }

}
