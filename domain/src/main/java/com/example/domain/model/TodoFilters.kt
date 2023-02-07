package com.example.domain.model

enum class TodoFilterPeriod {
    NO_PERIOD, TODAY, TOMORROW, THIS_WEEK
}

enum class TodoFilterAdditionalConditions {
    HIDE_COMPLETED, HIDE_WITHOUT_DEADLINE
}

data class TodoFilters(
    val selectedCategoriesId: Set<Long>,
    val period: TodoFilterPeriod,
    val additionalConditions: Set<TodoFilterAdditionalConditions>
)

