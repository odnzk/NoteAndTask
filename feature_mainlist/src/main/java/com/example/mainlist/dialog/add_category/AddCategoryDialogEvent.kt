package com.example.mainlist.dialog.add_category

import com.noteapp.model.Category

sealed interface AddCategoryDialogEvent {
    data class AddCategory(val category: Category) : AddCategoryDialogEvent
}
