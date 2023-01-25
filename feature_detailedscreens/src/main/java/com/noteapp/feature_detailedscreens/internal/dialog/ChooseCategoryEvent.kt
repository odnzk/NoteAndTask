package com.noteapp.feature_detailedscreens.internal.dialog

import com.noteapp.model.Category

internal sealed interface ChooseCategoryEvent {
    data class UpdateCategory(val category: Category) : ChooseCategoryEvent
    data class DeleteNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
    data class AddNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
}


