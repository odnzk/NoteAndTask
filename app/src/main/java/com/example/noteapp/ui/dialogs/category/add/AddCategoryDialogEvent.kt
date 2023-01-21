package com.example.noteapp.ui.dialogs.category.add

import com.example.domain.model.Category

sealed interface AddCategoryDialogEvent {
    data class AddCategory(val category: Category) : AddCategoryDialogEvent
}
