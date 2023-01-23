package com.example.domain.application.usecase.todo

import com.example.domain.model.Todo
import com.example.domain.model.TodoSortOrder
import com.example.domain.repository.TodoRepository
import com.example.domain.util.exceptions.isSameDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class GetAllTodos(private val todoRepository: TodoRepository) {

    operator fun invoke(todoSort: TodoSortOrder = TodoSortOrder.DEFAULT): Flow<List<Todo>> =
        when (todoSort) {
            TodoSortOrder.DEFAULT -> todoRepository.getAll()
            TodoSortOrder.BY_DEADLINE -> todoRepository.getAll()
                .map { it.sortedBy { todo -> todo.deadlineDate } }
            TodoSortOrder.TODAY -> {
                val calendar = Calendar.getInstance()
                todoRepository.getAll()
                    .map {
                        it.filter { todo ->
                            todo.deadlineDate.let {
                                val todoCalendar = Calendar.getInstance().apply {
                                    time = it
                                }
                                todoCalendar.isSameDay(calendar)
                            }
                        }
                    }
            }
            TodoSortOrder.THIS_WEEK -> {
                val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 7) }
                todoRepository.getAll()
                    .map { it.filter { todo -> todo.deadlineDate?.let { it.time < calendar.timeInMillis } == true } }
            }
        }
}
