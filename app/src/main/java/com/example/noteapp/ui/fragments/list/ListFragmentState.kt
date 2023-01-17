package com.example.noteapp.ui.fragments.list

import com.example.domain.model.NoteItem

data class ListFragmentState(
    private val searchQuery: String,
    private val noteItems: List<NoteItem>
)
