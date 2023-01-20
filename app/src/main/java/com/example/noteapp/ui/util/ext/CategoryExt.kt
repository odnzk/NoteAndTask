package com.example.noteapp.ui.util.ext

import android.content.Context
import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.domain.model.Category
import com.example.noteapp.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


// todo @ColorInt annotation everywhere
fun Category.toChipCategory(context: Context, onCategoryChipClick: (() -> Unit)? = null): Chip =
    Chip(ContextThemeWrapper(context, R.style.ChipCategoryStyle), null, 0).apply {
        id = View.generateViewId()
        layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        text = title
        chipBackgroundColor = ColorStateList.valueOf(color)
        setOnClickListener { onCategoryChipClick?.invoke() }
    }

fun List<Category>.categoriesToFlowCategories(
    constraintLayout: ConstraintLayout,
    flow: Flow,
    onCategoryChipClick: ((Long) -> Unit)? = null
) {
    forEach { category ->
        val categoryChip = category.toChipCategory(constraintLayout.context) {
            onCategoryChipClick?.invoke(category.id)
        }
        constraintLayout.addView(categoryChip)
        flow.addView(categoryChip)
    }
}

fun List<Category>.initCategoriesChipGroup(
    chipGroup: ChipGroup,
    onCategoryChipClick: ((Long) -> Unit)? = null
) {
    chipGroup.removeAllViews()
    forEach { category ->
        val categoryChip = category.toChipCategory(chipGroup.context) {
            onCategoryChipClick?.invoke(category.id)
        }
        chipGroup.addView(categoryChip)
    }
}
