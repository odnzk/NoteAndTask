package com.noteapp.feature_todolist.internal.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.Todo
import com.noteapp.core.ext.addButIfExistRemove
import com.noteapp.core.state.UiState
import com.noteapp.feature_todolist.internal.model.UiTodoFilters
import com.noteapp.feature_todolist.internal.util.mappers.toTodoFilters
import com.noteapp.ui.mappers.toUiCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListTodoViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val todoUseCases: TodoUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _todos: MutableStateFlow<UiState<List<Todo>>> = MutableStateFlow(UiState.Loading())
    val todos = _todos.asStateFlow()

    private var _todoFilters: MutableStateFlow<UiTodoFilters> = MutableStateFlow(
        savedStateHandle.get<UiTodoFilters>(
            TODO_FILTERS_KEY
        ) ?: UiTodoFilters()
    )
    val todoFilters = _todoFilters.asStateFlow()

    private var recentlyRemoved: Todo? = null
    private var jobObservingTodoList: Job? = null

    init {
        observeCategories()
        loadData()
    }

    private fun observeCategories() =
        viewModelScope.launch {
            categoryUseCases.getAllCategories().distinctUntilChanged()
                .collectLatest { repoCategories ->
                    _todoFilters.update { filters ->
                        filters.copy(categories = repoCategories.toUiCategories(filters.selectedCategoryIds))
                    }
                }
        }


    private fun loadData() {
        jobObservingTodoList?.cancel()
        jobObservingTodoList = viewModelScope.launch {
            todoUseCases.getAllTodos(_todoFilters.value.toTodoFilters()).distinctUntilChanged()
                .collectLatest { newList ->
                    _todos.update {
                        UiState.Success(newList)
                    }
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
                _todoFilters.update { it.copy(period = event.todoPeriod) }
                loadData()
            }
            is ListTodoEvent.UpdateTodoAdditionalFilters -> {
                _todoFilters.update { filters ->
                    filters.copy(
                        additionalConditions
                        = filters.additionalConditions.addButIfExistRemove(event.newCondition)
                    )
                }
                loadData()
            }
            is ListTodoEvent.UpdateSelectedCategoriesId -> {
                _todoFilters.update { filters ->
                    filters.copy(
                        selectedCategoryIds
                        = filters.selectedCategoryIds.addButIfExistRemove(event.newId)
                    )
                }
                loadData()
            }
            ListTodoEvent.SaveTodoFilters -> saveTodoFilters()
        }
    }

    private fun saveTodoFilters() {
        savedStateHandle[TODO_FILTERS_KEY] = _todoFilters.value
    }

    companion object {
        private const val TODO_FILTERS_KEY = "todo-filters"
    }
}
