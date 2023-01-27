package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.model.TodoFilterAdditionalConditions
import com.example.domain.model.TodoFilters
import com.example.domain.model.TodoPeriod
import com.example.domain.repository.TodoRepository
import com.example.domain.util.sorting.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllTodos(private val todoRepository: TodoRepository) {

    operator fun invoke(todoFilters: TodoFilters): Flow<List<Todo>> {
        var resList = if (todoFilters.selectedCategoriesId.isEmpty()) {
            todoRepository.getAll()
        } else {
            todoRepository.getByCategoriesId(todoFilters.selectedCategoriesId)
        }
        resList = when (todoFilters.todoFilterPeriod) {
            TodoPeriod.TOMORROW -> resList.map { TomorrowSorter.sort(it) }
            TodoPeriod.NO_PERIOD -> resList.map { DefaultSorter.sort(it) }
            TodoPeriod.TODAY -> resList.map { TodaySorter.sort(it) }
            TodoPeriod.THIS_WEEK -> resList.map { ThisWeekSorter.sort(it) }
        }
        // HIDE_COMPLETED, HIDE_WITHOUT_DEADLINE
        todoFilters.additionalConditions.forEach {
            resList = when (it) {
                TodoFilterAdditionalConditions.HIDE_COMPLETED ->
                    resList.map { HideCompleted.filter(it) }
                TodoFilterAdditionalConditions.HIDE_WITHOUT_DEADLINE ->
                    resList.map { HideWithoutDeadline.filter(it) }
            }
        }
        return resList
    }
}
