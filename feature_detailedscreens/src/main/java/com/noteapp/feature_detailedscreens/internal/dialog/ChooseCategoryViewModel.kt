package com.noteapp.feature_detailedscreens.internal.dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import com.noteapp.core.model.CategoryOwnerType
import com.noteapp.model.Category
import com.noteapp.model.Note
import com.noteapp.model.NoteItem
import com.noteapp.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private var _uiCategoryList: MutableStateFlow<List<UiCategory>> = MutableStateFlow(emptyList())
    val uiCategoryList = _uiCategoryList.asStateFlow()

    init {
        viewModelScope.launch {
            val noteItemResult: Result<NoteItem> = when (type) {
                CategoryOwnerType.NOTE_TYPE -> noteUseCases.getNoteById(noteItemId)
                CategoryOwnerType.TODO_TYPE -> todoUseCases.getTodoById(noteItemId)
            }
            noteItemResult.fold(onSuccess = { observeCategories(it) },
                onFailure = {
                // todo somehow show validation exception
            })
        }
    }

    private suspend fun observeCategories(noteItem: NoteItem) {
        categoryUseCases.getAllCategories().distinctUntilChanged()
            .collectLatest { categories ->
                when (noteItem) {
                    is Note -> {
                        val selectedCategories: List<Category> = noteItem.categories
                        _uiCategoryList.value = categories.map { category ->
                            UiCategory(
                                category,
                                isSelected = category in selectedCategories
                            )
                        }
                    }
                    is Todo -> {
                        noteItem.category?.id?.let { noteCategoryId ->
                            _uiCategoryList.value = categories.map {
                                UiCategory(
                                    it, it.id == noteCategoryId
                                )
                            }
                        } ?: run {
                            _uiCategoryList.value = categories.map {
                                UiCategory(
                                    it
                                )
                            }
                        }
                    }
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
                    CategoryOwnerType.NOTE_TYPE -> noteUseCases.removeNoteCategory(
                        noteItemId, event.categoryId
                    )
                    CategoryOwnerType.TODO_TYPE -> todoUseCases.removeTodoCategory(
                        noteItemId
                    )
                }
            }
            is ChooseCategoryEvent.AddNoteItemCategory -> {
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
    }

