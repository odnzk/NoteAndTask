package com.example.noteapp.ui.fragments.list

import com.example.domain.model.Category
import com.example.domain.model.NoteItem

data class ListFragmentState(
    val noteItems: List<NoteItem> = emptyList(),
    val categories: List<Category> = emptyList()
)
