package com.noteapp.ui.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.domain.model.Category
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.noteapp.ui.R
import kotlin.math.roundToInt


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
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                color, // unchecked
                makeDarkerColor(color) // checked
            )
        )
        chipBackgroundColor = colorStateList
//        chipBackgroundColor = ColorStateList.valueOf(color)
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

@ColorInt
private fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
    val alpha = (Color.alpha(color) * factor).roundToInt()
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    return Color.argb(alpha, red, green, blue)
}

private fun makeDarkerColor(color: Int, factor: Float = 0.9f): Int {
    val a = Color.alpha(color)
    val r = (Color.red(color) * factor).roundToInt()
    val g = (Color.green(color) * factor).roundToInt()
    val b = (Color.blue(color) * factor).roundToInt()
    return Color.argb(
        a,
        r / 5,
        g / 5,
        b / 5
    )
}


