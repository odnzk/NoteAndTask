package com.example.noteapp.ui.fragments.list

import com.example.domain.model.NoteItem
import com.example.domain.model.Filter
import com.example.domain.model.SortOrder

sealed interface ListFragmentEvent {
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) :
        ListFragmentEvent

    data class DeleteItem(val noteItem: NoteItem) : ListFragmentEvent
    object RestoreItem : ListFragmentEvent
    object ClearAll : ListFragmentEvent

    data class UpdateSortOrder(val sortOrder: SortOrder) : ListFragmentEvent
    data class UpdateFilter(val filter: Filter) : ListFragmentEvent
}



