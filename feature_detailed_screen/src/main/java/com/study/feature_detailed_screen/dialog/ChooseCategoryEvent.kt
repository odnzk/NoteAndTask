package com.study.feature_detailed_screen.dialog

import com.example.domain.model.Category

internal sealed interface ChooseCategoryEvent {
    data class UpdateCategory(val category: Category) : ChooseCategoryEvent
    data class DeleteNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
    data class AddNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
}


