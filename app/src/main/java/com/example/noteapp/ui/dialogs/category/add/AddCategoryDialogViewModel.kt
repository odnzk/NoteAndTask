package com.example.noteapp.ui.dialogs.category.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryDialogViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    // todo check
    private var _categories = emptyList<Category>()
    val categories: List<Category> = _categories

    init {
        viewModelScope.launch {
            _categories = categoryUseCases.getAllCategories().first()
        }
    }

    fun onEvent(event: AddCategoryDialogEvent) = viewModelScope.launch {
        when (event) {
            is AddCategoryDialogEvent.AddCategory -> categoryUseCases.addCategory(event.category)
        }
    }
}
