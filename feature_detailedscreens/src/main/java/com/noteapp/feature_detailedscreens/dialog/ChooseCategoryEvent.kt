package com.noteapp.feature_detailedscreens.dialog

import com.noteapp.model.Category

sealed interface ChooseCategoryEvent {
    data class UpdateCategory(val category: Category) : ChooseCategoryEvent
    data class DeleteNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
    data class AddNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
}


