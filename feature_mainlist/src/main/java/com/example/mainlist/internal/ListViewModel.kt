package com.example.mainlist.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.both.UnitedUseCases
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.*
import com.noteapp.core.ext.addButIfExistRemove
import com.noteapp.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val todoUseCases: TodoUseCases,
    categoryUseCases: CategoryUseCases,
    private val unitedUseCases: UnitedUseCases
) :
    ViewModel() {

    private var _categories: Flow<List<Category>> = categoryUseCases.getAllCategories()
    val categories = _categories

    private var _list: MutableStateFlow<UiState<List<NoteItem>>> =
        MutableStateFlow(UiState.Loading())
    val list = _list.asStateFlow()

    private var filterInfo: MutableStateFlow<FiltersInfo> = MutableStateFlow(FiltersInfo())
    private var recentlyRemoved: NoteItem? = null
    private var jobObservingNoteItemList: Job? = null


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
                ListFragmentEvent.Reload -> updateNoteItemList(filterInfo.value)
                is ListFragmentEvent.UpdateSelectedCategoriesId -> {
                    filterInfo.update {
                        it.copy(
                            selectedCategoriesId
                            = it.selectedCategoriesId.addButIfExistRemove(event.id)
                        )
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
                    _list.update { UiState.Success(noteItems) }
                }
        }
    }

}
