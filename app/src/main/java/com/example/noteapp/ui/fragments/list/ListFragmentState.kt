package com.example.noteapp.ui.fragments.list

import com.example.domain.model.Category
import com.example.domain.model.NoteItem

// separated because both list can have 3 states: Loading, Success, Error
data class ListFragmentState(
    val noteItems: List<NoteItem> = emptyList(),
    val categories: List<Category> = emptyList()
)
