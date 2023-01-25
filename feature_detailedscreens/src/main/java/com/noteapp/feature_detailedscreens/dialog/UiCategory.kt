package com.noteapp.feature_detailedscreens.dialog

import com.noteapp.model.Category

// helper ui model for choosing categories
data class UiCategory(val category: Category, val isSelected: Boolean = false)
