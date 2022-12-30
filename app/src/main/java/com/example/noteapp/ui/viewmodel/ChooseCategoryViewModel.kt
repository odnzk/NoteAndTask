package com.example.noteapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import com.example.noteapp.model.UiCategory
import com.example.noteapp.ui.fragments.events.ChooseCategoryEvent
import com.example.noteapp.ui.util.CategoryOwnerType
import com.example.noteapp.ui.util.UiState
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseCategoryViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val todoRepository: TodoRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val type: CategoryOwnerType by lazy {
        state.get<CategoryOwnerType>("type") ?: throw InvalidNavArgumentsException()
    }
    private val noteItemId: Long? by lazy {
        type.let { state.get<Long>(it.key) }
    }

    private var _uiCategoryList: MutableStateFlow<UiState<List<UiCategory>>> =
        MutableStateFlow(UiState.Loading())
    val noteItem: StateFlow<UiState<List<UiCategory>>> = _uiCategoryList

    init {
        viewModelScope.launch {
            when (type) {
                CategoryOwnerType.NOTE_TYPE -> noteItemId?.let { noteRepository.getById(it) }
                CategoryOwnerType.TODO_TYPE -> noteItemId?.let { todoRepository.getById(it) }
            }
        }
    }

    fun onEvent(event: ChooseCategoryEvent) = viewModelScope.launch {
        when (event) {
            is ChooseCategoryEvent.UpdateCategory -> {
                categoryRepository.update(event.category)
            }
            is ChooseCategoryEvent.DeleteNoteItemCategory -> {
//                when (type) {
//                    CategoryOwnerType.NOTE_TYPE -> noteItemId?.let {
//                        noteRepository.removeCategory(
//                            it,
//                            event.categoryId
//                        )
//                    }
//                    CategoryOwnerType.TODO_TYPE -> noteItemId?.let { todoRepository.getById(it) } // todo
//                }
            }
            is ChooseCategoryEvent.AddNoteItemCategory -> {
                when (type) {
                    CategoryOwnerType.NOTE_TYPE -> noteItemId?.let {
                        noteRepository.addCategory(
                            it,
                            event.categoryId
                        )
                    }
                    CategoryOwnerType.TODO_TYPE -> noteItemId?.let { todoRepository.getById(it) } // todo
                }
            }
        }
    }
}
