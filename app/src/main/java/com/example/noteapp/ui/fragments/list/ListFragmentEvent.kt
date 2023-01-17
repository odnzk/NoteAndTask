package com.example.noteapp.ui.fragments.list

import com.example.domain.model.Filter
import com.example.domain.model.NoteItem

sealed interface ListFragmentEvent {
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) :
        ListFragmentEvent

    data class DeleteItem(val noteItem: NoteItem) : ListFragmentEvent
    object RestoreItem : ListFragmentEvent
    object ClearAll : ListFragmentEvent

    // todo
    data class UpdateFilter(val filter: Filter) : ListFragmentEvent
    data class UpdateSearchQuery(val query: String) : ListFragmentEvent
}



