package com.study.feature_detailed_screen.dialog

import com.example.domain.model.Category

internal sealed interface ChooseCategoryEvent {
    class UpdateCategory(val category: Category) : ChooseCategoryEvent
    class DeleteNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
    class AddNoteItemCategory(val categoryId: Long) : ChooseCategoryEvent
}


