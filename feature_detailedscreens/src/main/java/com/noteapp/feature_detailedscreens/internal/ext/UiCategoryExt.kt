package com.noteapp.feature_detailedscreens.internal.ext

import android.util.Log
import com.google.android.material.chip.ChipGroup
import com.noteapp.feature_detailedscreens.internal.dialog.UiCategory
import com.noteapp.ui.R
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
            if (uiCategory.isSelected) {
                Log.d("hello", "checked") // todo
                setChipIconResource(R.drawable.ic_baseline_check_24)
            }
//            setCheckedIconResource(R.drawable.ic_baseline_add_24)
//            isCheckedIconVisible = true
        }
        chipGroup.addView(chipCategory)
    }
}
