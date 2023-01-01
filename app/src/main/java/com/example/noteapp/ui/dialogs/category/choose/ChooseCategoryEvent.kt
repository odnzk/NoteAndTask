package com.example.noteapp.ui.dialogs.category.choose

import com.example.domain.model.Category

sealed interface ChooseCategoryEvent {
    data class UpdateCategory(val category: Category) : ChooseCategoryEvent //??
    data class DeleteNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
    data class AddNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
}


