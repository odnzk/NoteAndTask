package com.example.mainlist.list

import com.example.domain.model.NoteItem
import com.example.domain.model.NoteItemFilter

internal sealed interface ListFragmentEvent {
    class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) :
        ListFragmentEvent

    class DeleteItem(val noteItem: NoteItem) : ListFragmentEvent
    class UpdateFilter(val filter: NoteItemFilter) : ListFragmentEvent
    class UpdateSearchQuery(val query: String) : ListFragmentEvent
    class UpdateSelectedCategoriesId(val id: Long) : ListFragmentEvent
    class DeleteCategory(val categoryId: Long) : ListFragmentEvent
    object RestoreItem : ListFragmentEvent
    object ClearAll : ListFragmentEvent
    object Reload : ListFragmentEvent
}



