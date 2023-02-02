package com.noteapp.feature_todolist.internal.list

import com.example.domain.model.Todo
import com.example.domain.model.TodoFilterAdditionalConditions
import com.example.domain.model.TodoPeriod

internal sealed interface ListTodoEvent {
    data class DeleteItem(val todo: Todo) : ListTodoEvent
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) : ListTodoEvent

    // filters
    data class UpdateTodoFilterPeriod(val todoPeriod: TodoPeriod) : ListTodoEvent
    data class UpdateSelectedCategoriesId(val newId: Long) : ListTodoEvent
    data class UpdateTodoAdditionalFilters(val newCondition: TodoFilterAdditionalConditions) :
        ListTodoEvent


    object TryAgain : ListTodoEvent
    object RestoreItem : ListTodoEvent
    object SaveTodoFilters : ListTodoEvent
}
