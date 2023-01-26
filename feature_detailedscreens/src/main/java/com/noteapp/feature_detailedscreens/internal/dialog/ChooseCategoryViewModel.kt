package com.noteapp.feature_detailedscreens.internal.dialog

import android.util.Log
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

    private var _uiCategoryList: MutableStateFlow<Result<List<UiCategory>>> = MutableStateFlow(
        Result.success(emptyList())
    )
    val uiCategoryList = _uiCategoryList.asStateFlow()

    init {
        viewModelScope.launch {
            val noteItemResult: Result<NoteItem> = when (type) {
                CategoryOwnerType.NOTE_TYPE -> noteUseCases.getNoteById(noteItemId)
                CategoryOwnerType.TODO_TYPE -> todoUseCases.getTodoById(noteItemId)
            }
            noteItemResult.fold(onSuccess = { observeCategories(it) }, onFailure = { error ->
                // NotFoundException
                _uiCategoryList.update { Result.failure(error) }
            })
        }
    }

    private suspend fun observeCategories(noteItem: NoteItem) {
        categoryUseCases.getAllCategories().distinctUntilChanged()
            .collectLatest { categories ->
                when (noteItem) {
                    is Note -> {
                        val selectedCategories: List<Category> = noteItem.categories
                        _uiCategoryList.update {
                            val uiCategories: List<UiCategory> = categories.map { category ->
                                UiCategory(category, category in selectedCategories)
                            }
                            Result.success(uiCategories)
                        }
                    }
                    is Todo -> {
                        _uiCategoryList.update {
                            val uiCategories = noteItem.category?.id?.let { noteCategoryId ->
                                categories.map { category ->
                                    UiCategory(category, category.id == noteCategoryId)
                                }
                            } ?: run {
                                categories.map { UiCategory(it) }
                            }
                            Result.success(uiCategories)
                        }
                    }
                }
            }
    }

    fun onEvent(event: ChooseCategoryEvent) = viewModelScope.launch {
        when (event) {
            is ChooseCategoryEvent.UpdateCategory ->
                categoryUseCases.updateCategory(event.category)
            is ChooseCategoryEvent.DeleteNoteItemCategory ->
                when (type) {
                    CategoryOwnerType.NOTE_TYPE -> noteUseCases.removeNoteCategory(
                        noteItemId, event.categoryId
                    )
                    CategoryOwnerType.TODO_TYPE -> todoUseCases.removeTodoCategory(
                        noteItemId
                    )
                }
            is ChooseCategoryEvent.AddNoteItemCategory ->
                when (type) {
                    CategoryOwnerType.NOTE_TYPE -> noteUseCases.addNoteCategory(
                        noteItemId, event.categoryId
                    )
                    CategoryOwnerType.TODO_TYPE -> todoUseCases.addTodoCategory(
                        noteItemId, event.categoryId
                    )
                }

        }
        }
    }

