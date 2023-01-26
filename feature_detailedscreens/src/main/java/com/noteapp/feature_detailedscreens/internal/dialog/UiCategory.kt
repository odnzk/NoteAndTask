package com.noteapp.feature_detailedscreens.internal.dialog

import com.example.domain.model.Category

// helper ui model for choosing categories
internal data class UiCategory(val category: Category, val isSelected: Boolean = false)
