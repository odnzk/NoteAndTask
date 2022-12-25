package com.example.noteapp.ui.fragments.events

import com.example.domain.model.NoteItem
import com.example.domain.util.Filter
import com.example.domain.util.SortOrder

sealed interface ListFragmentEvent {
    data class AddItem(val noteItem: NoteItem) : ListFragmentEvent
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) :
        ListFragmentEvent

    data class DeleteItem(val noteItem: NoteItem) : ListFragmentEvent
    object RestoreItem : ListFragmentEvent
    object ClearAll : ListFragmentEvent

    data class UpdateSortOrder(val sortOrder: SortOrder) : ListFragmentEvent
    data class UpdateFilter(val filter: Filter) : ListFragmentEvent
}



