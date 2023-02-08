package com.example.mainlist.internal

import com.example.domain.model.NoteItem
import com.example.domain.model.NoteItemFilter

internal sealed interface ListFragmentEvent {
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) :
        ListFragmentEvent

    data class DeleteItem(val noteItem: NoteItem) : ListFragmentEvent
    data class UpdateFilter(val filter: NoteItemFilter) : ListFragmentEvent
    data class UpdateSearchQuery(val query: String) : ListFragmentEvent
    data class UpdateSelectedCategoriesId(val id: Long) : ListFragmentEvent

    object RestoreItem : ListFragmentEvent
    object ClearAll : ListFragmentEvent
    object Reload : ListFragmentEvent

}



