package com.example.mainlist.dialog.add_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.noteapp.core.CompletableState
import com.noteapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryDialogViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private var _category: MutableStateFlow<CompletableState<Category>> =
        MutableStateFlow(CompletableState.InProgress(Category.defaultInstance()))
    val category = _category.asStateFlow()

    fun onEvent(event: AddCategoryDialogEvent) = viewModelScope.launch {
        when (event) {
            is AddCategoryDialogEvent.AddCategory -> categoryUseCases.addCategory(event.category)
                .fold(
                    onSuccess = {
                        _category.value = CompletableState.Completed(_category.value.data)
                    },
                    onFailure = { error ->
                        _category.value = CompletableState.Error(
                            data = _category.value.data,
                            error = error
                        )
                    }
                )
        }
    }
}
