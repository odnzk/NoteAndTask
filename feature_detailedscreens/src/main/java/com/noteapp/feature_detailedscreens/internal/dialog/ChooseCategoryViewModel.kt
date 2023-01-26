package com.noteapp.feature_detailedscreens.internal.dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.Category
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.domain.model.Todo
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import com.noteapp.core.model.CategoryOwnerType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ChooseCategoryViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val noteUseCases: NoteUseCases,
    private val todoUseCases: TodoUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    val type: CategoryOwnerType by lazy {
        state.get<CategoryOwnerType>("type") ?: throw InvalidNavArgumentsException()
    }
    private val noteItemId: Long by lazy {
        type.let { state.get<Long>(it.key) ?: throw InvalidNavArgumentsException() }
    }

    // noteItem to observe, categoryList
    private var _noteItem: Flow<NoteItem> = when (type) {
        CategoryOwnerType.TODO_TYPE -> todoUseCases.getTodoFlowById(noteItemId).filterNotNull()
        CategoryOwnerType.NOTE_TYPE -> noteUseCases.getNoteFlowById(noteItemId).filterNotNull()
    }
    private var categories: Flow<List<Category>> = categoryUseCases.getAllCategories()

    private var _uiCategoryList: MutableStateFlow<Result<List<UiCategory>>> = MutableStateFlow(
        Result.success(emptyList())
    )
    val uiCategoryList = _uiCategoryList.asStateFlow()

    init {
        viewModelScope.launch {
            combine(categories, _noteItem) { categories, noteItem ->
                _uiCategoryList.update {
                    Result.success(updateUiCategories(noteItem, categories))
                }
            }.collect()
        }
    }

    private fun updateUiCategories(
        noteItem: NoteItem,
        categories: List<Category>
    ): List<UiCategory> {
        return when (noteItem) {
            is Note -> {
                val selectedCategories: List<Category> = noteItem.categories
                val uiCategories: List<UiCategory> = categories.map { category ->
                    UiCategory(category, category in selectedCategories)
                }
                uiCategories
            }
            is Todo -> {
                val uiCategories = noteItem.category?.id?.let { noteCategoryId ->
                    categories.map { category ->
                        UiCategory(category, category.id == noteCategoryId)
                    }
                } ?: run {
                    categories.map { UiCategory(it) }
                }
                uiCategories
            }
        }
    }

    fun onEvent(event: ChooseCategoryEvent) = viewModelScope.launch {
        when (event) {
            is ChooseCategoryEvent.UpdateCategory -> categoryUseCases.updateCategory(event.category)
            is ChooseCategoryEvent.DeleteNoteItemCategory -> when (type) {
                CategoryOwnerType.NOTE_TYPE -> noteUseCases.removeNoteCategory(
                    noteItemId, event.categoryId
                )
                CategoryOwnerType.TODO_TYPE -> todoUseCases.removeTodoCategory(
                    noteItemId
                )
            }
            is ChooseCategoryEvent.AddNoteItemCategory -> {
                when (type) {
                    CategoryOwnerType.NOTE_TYPE -> noteUseCases.addNoteCategory(
                        noteId = noteItemId,
                        categoryId = event.categoryId
                    )
                    CategoryOwnerType.TODO_TYPE -> todoUseCases.updateTodoCategory(
                        todoId = noteItemId,
                        newCategoryId = event.categoryId
                    )
                }

            }
        }
        }
    }

