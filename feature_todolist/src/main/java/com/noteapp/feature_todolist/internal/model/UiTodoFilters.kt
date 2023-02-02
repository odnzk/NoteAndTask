package com.noteapp.feature_todolist.internal.model

import android.os.Parcelable
import com.example.domain.model.TodoFilterAdditionalConditions
import com.example.domain.model.TodoPeriod
import com.noteapp.ui.model.UiCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiTodoFilters(
    val categories: Set<UiCategory> = emptySet(), // UiCategories
    val todoFilterPeriod: TodoPeriod = TodoPeriod.NO_PERIOD,
    val additionalConditions: Set<TodoFilterAdditionalConditions> = emptySet()
) : Parcelable

