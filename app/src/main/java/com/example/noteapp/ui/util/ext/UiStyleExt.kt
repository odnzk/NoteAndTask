package com.example.noteapp.ui.util.ext


import android.content.Context
import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Category
import com.example.noteapp.R
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.*


//private const val CATEGORY_NOT_SELECTED_COLOR_ALPHA = 30

fun RecyclerView.initStandardVerticalRecyclerView() {
    this.apply {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
}

// todo @ColorInt annotation everywhere
fun Category.toChipCategory(context: Context, onCategoryChipClick: (() -> Unit)? = null): Chip =
    Chip(ContextThemeWrapper(context, R.style.ChipCategoryStyle), null, 0).apply {
        id = View.generateViewId()
        layoutParams = ConstraintLayout.LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT
        )
        text = title
        chipBackgroundColor = ColorStateList.valueOf(color)
        setOnClickListener { onCategoryChipClick?.invoke() }
    }

fun List<Category>.categoriesToFlowCategories(
    constraintLayout: ConstraintLayout,
    flow: Flow,
    onCategoryChipClick: (() -> Unit)? = null
) {
    forEach { category ->
        val categoryChip = category.toChipCategory(constraintLayout.context) {
            onCategoryChipClick?.invoke()
        }
        constraintLayout.addView(categoryChip)
        flow.addView(categoryChip)
    }
}

fun Date.formatToTodoDate(pattern: String? = "EE, dd.MM"): String =
    SimpleDateFormat(pattern).format(this)

fun Calendar.formatToReminderString(): String =
    SimpleDateFormat("Reminder dd.MM.yy at HH:mm").format(this)

fun Date.formatToNoteDate(pattern: String? = "dd.MM.yy"): String =
    SimpleDateFormat(pattern).format(this)


