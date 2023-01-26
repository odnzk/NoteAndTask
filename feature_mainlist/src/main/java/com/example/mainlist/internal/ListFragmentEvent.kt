package com.example.mainlist.internal

import com.example.domain.model.Filter
import com.example.domain.model.NoteItem

internal sealed interface ListFragmentEvent {
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) :
        ListFragmentEvent

    data class DeleteItem(val noteItem: NoteItem) : ListFragmentEvent
    data class UpdateFilter(val filter: Filter) : ListFragmentEvent
    data class UpdateSearchQuery(val query: String) : ListFragmentEvent
    data class UpdateSelectedCategoryId(val id: Long) : ListFragmentEvent

    object RestoreItem : ListFragmentEvent
    object ClearAll : ListFragmentEvent
    object ReloadData : ListFragmentEvent

}



