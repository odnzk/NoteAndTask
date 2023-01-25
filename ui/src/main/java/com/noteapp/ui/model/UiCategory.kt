package com.noteapp.ui.model

import com.noteapp.model.Category

// for note and task detailed screens (while choosing another categories)
data class UiCategory(val category: Category, val isSelected: Boolean = false)
