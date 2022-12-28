package com.example.noteapp.ui.util.ext


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import androidx.constraintlayout.helper.widget.Flow
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.example.domain.model.Category
import com.example.domain.model.Note
import com.example.noteapp.R
import com.example.noteapp.model.UiCategory
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.*


private const val CATEGORY_NOT_SELECTED_COLOR_ALPHA = 30

fun Chip.setCategoryStyle(color: String, title: String): Chip =
    this.apply {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        text = title
        val selectedCategoryColor = Color.parseColor(color)
        ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checkable)
            ),
            intArrayOf(
                selectedCategoryColor,
                ColorUtils.setAlphaComponent(
                    selectedCategoryColor,
                    CATEGORY_NOT_SELECTED_COLOR_ALPHA
                )
            )
        ).also { chipBackgroundColor = it }
        ResourcesCompat.getFont(context, R.font.inter_regular).also { typeface = it }
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 65);
//        <item name="android:textSize">12sp</item>
//        <item name="android:fontFamily">@font/inter_light</item>
//        <item name="android:textColor">@color/white</item>
    }

fun Category.toChip(context: Context, onCategoryChipClick: () -> Unit) = Chip(context).apply {
    setCategoryStyle(color = color, title = title)
    setOnClickListener {
        onCategoryChipClick()
    }
}

fun UiCategory.toChip(context: Context) = Chip(context).apply {
    isSelected = this@toChip.isSelected
    setCategoryStyle(color = this@toChip.category.color, title = this@toChip.category.title)
}


fun Note.categoriesToFlowCategories(flow: Flow, onCategoryChipClick: () -> Unit) {
    categories.takeIf { it.isNotEmpty() }?.let {
        it.forEach { category ->
            flow.addView(category.toChip(flow.context) {
                onCategoryChipClick()
                // todo maybe replace to List<Category> extension
            })
        }
    }
}

fun List<UiCategory>.toFlowCategories(flow: Flow) {
    takeIf { it.isNotEmpty() }?.let {
        it.forEach { category ->
            flow.addView(category.toChip(flow.context))
        }
    }
}

fun List<Category>.insertToConstraintLayoutFlow(flow: Flow, onCategoryChipClick: () -> Unit) {
    takeIf { it.isNotEmpty() }?.let {
        it.forEach { category ->
            flow.addView(category.toChip(flow.context) {
                onCategoryChipClick()
            })
        }
    }
}

fun Date.convertTUiString(pattern: String? = "EE, dd.MM"): String =
    SimpleDateFormat(pattern).format(this)

