package com.example.noteapp.ui.dialogs.category.choose

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.noteapp.model.UiCategory
import com.example.noteapp.ui.util.CategoryOwnerType
import com.example.noteapp.ui.util.UiState
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseCategoryViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val noteUseCases: NoteUseCases,
    private val todoUseCases: TodoUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val type: CategoryOwnerType by lazy {
        state.get<CategoryOwnerType>("type") ?: throw InvalidNavArgumentsException()
    }
    private val noteItemId: Long by lazy {
        type.let { state.get<Long>(it.key) ?: throw InvalidNavArgumentsException() }
    }

    private var _uiCategoryList: MutableStateFlow<UiState<List<UiCategory>>> =
        MutableStateFlow(UiState.Loading())
    val noteItem: StateFlow<UiState<List<UiCategory>>> = _uiCategoryList.asStateFlow()

    init {
        viewModelScope.launch {
            when (type) {
                CategoryOwnerType.NOTE_TYPE -> noteItemId?.let { noteUseCases.getNoteById(it) }
                CategoryOwnerType.TODO_TYPE -> noteItemId?.let { todoUseCases.getTodoById(it) }
            }
        }
    }

    fun onEvent(event: ChooseCategoryEvent) = viewModelScope.launch {
        when (event) {
            is ChooseCategoryEvent.UpdateCategory -> {
                categoryUseCases.updateCategory(event.category)
            }
            is ChooseCategoryEvent.DeleteNoteItemCategory -> {
                when (type) {
                    CategoryOwnerType.NOTE_TYPE -> noteItemId?.let {
                        noteUseCases.removeNoteCategory(it, event.categoryId)
                    }
                    CategoryOwnerType.TODO_TYPE -> noteItemId?.let {
                        todoUseCases.removeTodoCategory(it)
                    }
                }
            }
            is ChooseCategoryEvent.AddNoteItemCategory -> {
                when (type) {
                    CategoryOwnerType.NOTE_TYPE -> noteItemId?.let {
                        noteUseCases.addNoteCategory(it, event.categoryId)
                    }
                    CategoryOwnerType.TODO_TYPE -> noteItemId?.let {
                        todoUseCases.addTodoCategory(
                            it,
                            event.categoryId
                        )
                    }
                }
            }
        }
    }
}
