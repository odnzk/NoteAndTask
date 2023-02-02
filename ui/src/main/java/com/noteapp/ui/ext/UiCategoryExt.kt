package com.noteapp.ui.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.domain.model.Category
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.noteapp.ui.R
import com.noteapp.ui.mappers.toCategory
import com.noteapp.ui.model.UiCategory


private const val WHITE_COLOR = "#FFFFFF"
private const val CATEGORY_FONT_SIZE = 20f

fun Category.toChipCategory(
    context: Context,
    isCheckedStyleEnabled: Boolean,
    onCategoryChipClick: (() -> Unit)? = null
): Chip =
    Chip(ContextThemeWrapper(context, R.style.ChipCategoryStyle), null, 0).apply {
        id = View.generateViewId()
        text = title
        chipBackgroundColor = ColorStateList.valueOf(color)

        setOnClickListener {
            onCategoryChipClick?.invoke()
        }

        if (isCheckedStyleEnabled) {
            isCheckable = true
            setCheckedIconResource(R.drawable.ic_baseline_check_24)
            isCheckedIconVisible = true
            checkedIconTint = ColorStateList.valueOf(Color.parseColor(WHITE_COLOR))
        }
    }

fun List<Category>.toChipGroup(
    chipGroup: ChipGroup,
    isCheckedStyleEnabled: Boolean = false,
    onAddCategoryClick: (() -> Unit)? = null,
    onCategoryChipClick: ((Long) -> Unit)? = null
) {
    chipGroup.removeAllViews()
    forEach { category ->
        val categoryChip = category.toChipCategory(chipGroup.context, isCheckedStyleEnabled) {
            onCategoryChipClick?.invoke(category.id)
        }
        chipGroup.addView(categoryChip)
    }
    onAddCategoryClick?.let {
        chipGroup.addView(Chip(chipGroup.context).setBtnAddCategoryStyle {
            onAddCategoryClick()
        })
    }
}


fun Chip.setBtnAddCategoryStyle(onAction: () -> Unit): Chip =
    Chip(ContextThemeWrapper(context, R.style.ChipCategoryStyle), null, 0).apply {
        layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setTextColor(Color.parseColor(WHITE_COLOR))
        setChipBackgroundColorResource(R.color.black)

        isCheckable = false
        setChipIconResource(R.drawable.ic_baseline_add_24)
        chipIconTint = ColorStateList.valueOf(Color.parseColor(WHITE_COLOR))
        chipIconSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            CATEGORY_FONT_SIZE,
            context.resources.displayMetrics
        )
        isChipIconVisible = true
        text = context.getText(R.string.add_category)
        setOnClickListener { onAction() }
    }

fun List<UiCategory>.toChipGroup(
    chipGroup: ChipGroup,
    onCategoryClick: ((Long) -> Unit)? = null
) {
    chipGroup.removeAllViews()
    forEach { uiCategory ->
        val chipCategory = uiCategory.toCategory().toChipCategory(chipGroup.context, true) {
            onCategoryClick?.invoke(uiCategory.id)
        }.apply {
            isChecked = uiCategory.isSelected
        }
        chipGroup.addView(chipCategory)
    }
}


