package com.example.mainlist.dialog

import com.example.domain.model.Category

internal sealed interface AddCategoryDialogEvent {
    class AddCategory(val category: Category) : AddCategoryDialogEvent
}
