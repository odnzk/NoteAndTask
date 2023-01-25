package com.example.mainlist.internal.dialog

import com.noteapp.model.Category

internal sealed interface AddCategoryDialogEvent {
    data class AddCategory(val category: Category) : AddCategoryDialogEvent
}
