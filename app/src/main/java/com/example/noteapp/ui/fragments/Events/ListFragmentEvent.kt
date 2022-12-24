package com.example.noteapp.ui.fragments.Events

import com.example.domain.model.NoteItem
import com.example.domain.model.Todo
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

sealed interface TodoDetailedEvent {
    data class UpdateTodo(val todo: Todo) : TodoDetailedEvent
    object DeleteTodo : TodoDetailedEvent
}

// ListFragmentEvent
// 1) update TodoItem checked state
// 2) clear all
// 3) add new TodoItem/NoteItem
// 4) update filters
// 5) swipe to delete + restore

// NoteDetailsEvent
// 1) update note
// 2) delete note
// TodoDetailFragment
// 1) update TodoItem
// 2) delete TodoItem



