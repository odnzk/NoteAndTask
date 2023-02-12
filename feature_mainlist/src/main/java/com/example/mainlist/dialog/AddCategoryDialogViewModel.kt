package com.example.mainlist.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.model.Category
import com.noteapp.core.state.CompletableState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddCategoryDialogViewModel @Inject constructor(
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
                        _category.update { CompletableState.Completed(it.data) }
                    },
                    onFailure = { error ->
                        _category.update {
                            CompletableState.Error(
                                data = it.data, error = error
                            )
                        }
                    }
                )
        }
    }
}
