package com.example.domain.application.usecase.todo

import com.example.domain.repository.TodoRepository
import com.example.domain.util.sorting.TodoSorter
import com.example.domain.model.Todo
import com.example.domain.model.TodoFilters
import com.example.domain.model.TodoPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllTodos(private val todoRepository: TodoRepository) {

    // todo
    operator fun invoke(todoFilters: TodoFilters): Flow<List<Todo>> =
        when (todoFilters.todoFilterPeriod) {
            TodoPeriod.TOMORROW -> todoRepository.getAll() // todo
            TodoPeriod.TODAY -> todoRepository.getAll()
                .map { TodoSorter.OnlyTodaySorter.sort(it) }
            TodoPeriod.THIS_WEEK -> todoRepository.getAll()
                .map { TodoSorter.ThisWeekSorter.sort(it) }
            TodoPeriod.NO_PERIOD -> todoRepository.getAll()
        }
}
