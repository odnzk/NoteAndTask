package com.noteapp.feature_detailedscreens.internal.ext

import com.google.android.material.chip.ChipGroup
import com.noteapp.feature_detailedscreens.internal.dialog.UiCategory
import com.noteapp.ui.ext.toChipCategory

internal fun List<UiCategory>.toChipGroup(
    chipGroup: ChipGroup,
    onCategoryClick: ((Long) -> Unit)? = null
) {
    chipGroup.removeAllViews()
    forEach { uiCategory ->
        val chipCategory = uiCategory.category.toChipCategory(chipGroup.context) {
            onCategoryClick?.invoke(uiCategory.category.id)
        }.apply {
            isChecked = uiCategory.isSelected
        }
        chipGroup.addView(chipCategory)
    }
}
