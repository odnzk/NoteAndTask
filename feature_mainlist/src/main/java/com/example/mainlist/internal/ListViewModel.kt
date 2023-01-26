package com.example.mainlist.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.both.UnitedUseCases
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.noteapp.core.state.UiState
import com.example.domain.model.FiltersInfo
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListViewModel @Inject constructor(
//    private val preferenceStorage: PreferenceStorage, // saved state handler
    private val noteUseCases: NoteUseCases,
    private val todoUseCases: TodoUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val unitedUseCases: UnitedUseCases
) :
    ViewModel() {

    private var _listState: MutableStateFlow<UiState<ListFragmentState>> =
        MutableStateFlow(UiState.Loading())
    val listState = _listState.asStateFlow()

    private var filterInfo: MutableStateFlow<FiltersInfo> = MutableStateFlow(FiltersInfo())
    private var recentlyRemoved: NoteItem? = null
    private var jobObservingNoteItemList: Job? = null

    init {
        loadData()
    }

    private fun loadData() =
        viewModelScope.launch {
            _listState.value = UiState.Loading()

            updateNoteItemList(filterInfo.value)

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

    fun onEvent(event: ListFragmentEvent) =
        viewModelScope.launch {
            when (event) {
                is ListFragmentEvent.DeleteItem -> {
                    when (val noteItem = event.noteItem) {
                        is Note -> noteUseCases.deleteNote(noteItem.id)
                        is Todo -> todoUseCases.deleteTodo(noteItem.id)
                    }
                    recentlyRemoved = event.noteItem
                }
                is ListFragmentEvent.RestoreItem -> {
                    recentlyRemoved?.let { noteItem ->
                        when (noteItem) {
                            is Note -> noteUseCases.addNote(noteItem)
                            is Todo -> todoUseCases.addTodo(noteItem)
                        }
                    }
                }
                is ListFragmentEvent.UpdateFilter -> {
                    // both, only notes, only todos
                    filterInfo.value = filterInfo.value.copy(filter = event.filter)
                    updateNoteItemList(filterInfo.value)
                }
                is ListFragmentEvent.ClearAll -> unitedUseCases.deleteAll()
                is ListFragmentEvent.UpdateTodoCompletedStatus -> {
                    todoUseCases.updateIsCompleted(event.todoId, event.isCompleted)
                }
                is ListFragmentEvent.UpdateSearchQuery -> {
                    filterInfo.value = filterInfo.value.copy(searchQuery = event.query)
                    updateNoteItemList(filterInfo.value)
                }
                ListFragmentEvent.ReloadData -> updateNoteItemList(filterInfo.value)
                is ListFragmentEvent.UpdateSelectedCategoryId -> {
                    if (filterInfo.value.selectedCategoryId == event.id) {
                        filterInfo.value = filterInfo.value.copy(selectedCategoryId = null)
                    } else {
                        filterInfo.value = filterInfo.value.copy(selectedCategoryId = event.id)
                    }
                    updateNoteItemList(filterInfo.value)
                }
            }
        }


    private fun updateNoteItemList(filterInfo: FiltersInfo) = run {
        jobObservingNoteItemList?.cancel()
        jobObservingNoteItemList = viewModelScope.launch {
            unitedUseCases.getBothTodosAndNotes(filterInfo).distinctUntilChanged()
                .collectLatest { noteItems ->
                    _listState.value.data?.let {
                        _listState.value = UiState.Success(it.copy(noteItems = noteItems))
                    } ?: run {
                        _listState.value = UiState.Success(ListFragmentState(noteItems = noteItems))
                    }
                }
        }
    }

}
