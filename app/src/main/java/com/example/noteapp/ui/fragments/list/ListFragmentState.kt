package com.example.noteapp.ui.fragments.list

import com.example.domain.model.Category
import com.example.domain.model.Filter
import com.example.domain.model.NoteItem

data class ListFragmentState(
    val searchQuery: String = "",
    val filter: Filter = Filter.BOTH,
    val selectedCategory: Category? = null,
    val noteItems: List<NoteItem> = emptyList(),
    val categories: List<Category> = emptyList()
)
