package com.example.domain.util.sorting

import com.example.domain.model.Todo
interface TodoSorterByAdditionalConditions {
    fun filter(items: List<Todo>): List<Todo>
}

object HideCompleted : TodoSorterByAdditionalConditions {
    override fun filter(items: List<Todo>): List<Todo> =
        items.filter { !it.isCompleted }
}

object HideWithoutDeadline : TodoSorterByAdditionalConditions {
    override fun filter(items: List<Todo>): List<Todo> =
        items.filter { it.deadlineDate != null }
}


