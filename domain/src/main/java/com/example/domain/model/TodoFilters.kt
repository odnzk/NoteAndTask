package com.example.domain.model

enum class TodoPeriod {
    NO_PERIOD, TODAY, TOMORROW, THIS_WEEK
}

enum class TodoFilterAdditionalConditions {
    HIDE_COMPLETED, HIDE_WITHOUT_DEADLINE
}

// hide completed
// hide without deadline
// sort by deadline
// selected categories (set)
// todoSortOrder
data class TodoFilters(
    val selectedCategoriesId: Set<Long> = emptySet(),
    val todoFilterPeriod: TodoPeriod = TodoPeriod.NO_PERIOD,
    val additionalConditions: Set<TodoFilterAdditionalConditions> = emptySet()
) {
    fun updateAdditionalConditions(newCondition: TodoFilterAdditionalConditions): Set<TodoFilterAdditionalConditions> =
        additionalConditions.toMutableSet().apply {
            if (newCondition in additionalConditions) {
                remove(newCondition)
            } else {
                add(newCondition)
            }
        }

    fun updateSelectedCategories(newCategoryId: Long): Set<Long> =
        selectedCategoriesId.toMutableSet().apply {
            if (newCategoryId in selectedCategoriesId) {
                remove(newCategoryId)
            } else {
                add(newCategoryId)
            }
        }
}
