package com.example.noteapp.ui.fragments.list

import com.example.domain.model.Category
import com.example.domain.model.Filter
import com.example.domain.model.NoteItem

data class ListFragmentState(
//    val searchQuery: String = "", // no loading/error state
//    val filter: Filter = Filter.BOTH, // no loading/error state
//    val selectedCategory: Category? = null, // no loading/error state
    val noteItems: List<NoteItem> = emptyList(),
    val categories: List<Category> = emptyList()
)
