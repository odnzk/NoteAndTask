package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import com.example.domain.util.sorting.TodoSorter
import com.noteapp.model.Todo
import com.noteapp.model.TodoSortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllTodos(private val todoRepository: TodoRepository) {

    operator fun invoke(todoSort: TodoSortOrder = TodoSortOrder.DEFAULT): Flow<List<Todo>> =
        when (todoSort) {
            TodoSortOrder.DEFAULT -> todoRepository.getAll()
            TodoSortOrder.BY_DEADLINE -> todoRepository.getAll()
                .map { TodoSorter.ByDeadlineSorter.sort(it) }
            TodoSortOrder.TODAY -> todoRepository.getAll()
                .map { TodoSorter.OnlyTodaySorter.sort(it) }
            TodoSortOrder.THIS_WEEK -> todoRepository.getAll()
                .map { TodoSorter.ThisWeekSorter.sort(it) }
        }
}
