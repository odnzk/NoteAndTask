package com.noteapp.feature_todolist.internal.model

import android.os.Parcelable
import com.example.domain.model.TodoFilterAdditionalConditions
import com.example.domain.model.TodoFilterPeriod
import com.noteapp.ui.model.UiCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiTodoFilters(
    val selectedCategoryIds: Set<Long> = emptySet(),
    val categories: List<UiCategory> = emptyList(),
    val period: TodoFilterPeriod = TodoFilterPeriod.NO_PERIOD,
    val additionalConditions: Set<TodoFilterAdditionalConditions> = emptySet()
) : Parcelable

