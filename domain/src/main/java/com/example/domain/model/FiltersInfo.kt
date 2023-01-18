package com.example.domain.model

// separated because instead of ListFragmentState they are stateless (only success or 'default' state)
data class FiltersInfo(
    val searchQuery: String = "", // no loading/error state
    val filter: Filter = Filter.BOTH, // no loading/error state
    val selectedCategory: Category? = null // no loading/error state
) {
}
enum class Filter { TODO_ONLY, NOTES_ONLY, BOTH }
