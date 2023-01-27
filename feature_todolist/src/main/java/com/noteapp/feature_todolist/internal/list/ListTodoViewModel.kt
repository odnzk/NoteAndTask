package com.noteapp.feature_todolist.internal.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.Todo
import com.example.domain.model.TodoFilters
import com.noteapp.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListTodoViewModel @Inject constructor(
    categoryUseCases: CategoryUseCases,
    private val todoUseCases: TodoUseCases
) : ViewModel() {

    private var _todos: MutableStateFlow<UiState<List<Todo>>> = MutableStateFlow(UiState.Loading())
    val todos = _todos.asStateFlow()

    private var _todoFilters: MutableStateFlow<TodoFilters> = MutableStateFlow(TodoFilters())
    val categories = categoryUseCases.getAllCategories()

    private var recentlyRemoved: Todo? = null
    private var jobObservingTodoList: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        jobObservingTodoList?.cancel()
        jobObservingTodoList = viewModelScope.launch {
            todoUseCases.getAllTodos(_todoFilters.value).distinctUntilChanged()
                .collectLatest { newList ->
                    _todos.value = UiState.Success(newList)
                    Log.d("hello", "updating state ${_todos.value.data}")
//                    _todos.update {
//                        UiState.Success(newList)
//                    }
                }
        }
    }

    fun onEvent(event: ListTodoEvent) = viewModelScope.launch {
        when (event) {
            is ListTodoEvent.TryAgain -> loadData()
            is ListTodoEvent.UpdateTodoCompletedStatus -> todoUseCases.updateIsCompleted(
                event.todoId, event.isCompleted
            )
            is ListTodoEvent.DeleteItem -> {
                recentlyRemoved = event.todo
                todoUseCases.deleteTodo(event.todo.id)
            }
            ListTodoEvent.RestoreItem -> recentlyRemoved?.let { todoUseCases.addTodo(it) }
            is ListTodoEvent.UpdateTodoFilterPeriod -> {
                _todoFilters.update { it.copy(todoFilterPeriod = event.todoPeriod) }
                loadData()
            }
            is ListTodoEvent.UpdateTodoAdditionalFilters -> {
                _todoFilters.update { filters ->
                    filters.copy(additionalConditions = filters.updateAdditionalConditions(event.newCondition))
                }
                loadData()
            }
            is ListTodoEvent.UpdateSelectedCategoriesId -> {
                _todoFilters.update { filters ->
                    filters.copy(selectedCategoriesId = filters.updateSelectedCategories(event.newId))
                }
                loadData()
            }
        }
    }

}
