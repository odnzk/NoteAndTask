package com.noteapp.ui.mappers

import com.example.domain.model.Category
import com.noteapp.ui.model.UiCategory

fun UiCategory.toCategory() = Category(id = id, title = title, color = color)

fun Category.toUiCategory(isSelected: Boolean = false) =
    UiCategory(id = id, title = title, color = color, isSelected = false)
