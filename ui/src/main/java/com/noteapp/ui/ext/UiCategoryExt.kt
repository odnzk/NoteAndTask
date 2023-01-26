package com.noteapp.ui.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import com.noteapp.ui.R
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.example.domain.model.Category

private const val WHITE_COLOR = "#FFFFFF"
private const val CATEGORY_FONT_SIZE = 20f

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


