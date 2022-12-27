package com.example.noteapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.fragments.events.ChooseCategoryEvent
import com.example.noteapp.ui.util.nav.CategoryOwnerType
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
//            state.get<CategoryOwnerType>("type")
        CategoryOwnerType.NOTE_TYPE
    }
    private val todoId: Long? by lazy { state.get<Long>("todoId") }

    private var _categories: MutableStateFlow<UiState<List<Category>>> =
        MutableStateFlow(UiState.Loading())
    val categories: StateFlow<UiState<List<Category>>> = _categories

    init {
        when (type) {
            CategoryOwnerType.NOTE_TYPE -> noteRepository.getAll() // get categories
            CategoryOwnerType.TODO_TYPE -> todoRepository.getAll() // get categories
        }
    }

    fun onEvent(event: ChooseCategoryEvent) = viewModelScope.launch {
        when (event) {
            is ChooseCategoryEvent.AddCategory -> {
                categoryRepository.add(event.category)
            }
            is ChooseCategoryEvent.UpdateCategory -> {
                categoryRepository.update(event.category)
            }
            is ChooseCategoryEvent.DeleteCategory -> {
                categoryRepository.deleteById(event.categoryId)
            }
            is ChooseCategoryEvent.UpdateNoteItemCategories -> {
                // todo
                // check what type -> find selected repository -> update categories with that table
            }
        }
    }
}
