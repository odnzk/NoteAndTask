package com.noteapp.feature_detailedscreens.dialog.ext

import com.google.android.material.chip.ChipGroup
import com.noteapp.feature_detailedscreens.dialog.UiCategory
import com.noteapp.ui.ext.toChipCategory

fun List<UiCategory>.toChipGroup(
    chipGroup: ChipGroup,
    onCategoryClick: ((Long) -> Unit)? = null
) {
    forEach { uiCategory ->
        val chipCategory = uiCategory.category.toChipCategory(chipGroup.context) {
            onCategoryClick?.invoke(uiCategory.category.id)
        }
        chipCategory.isChecked = uiCategory.isSelected
        chipGroup.addView(chipCategory)
    }
}
