package com.noteapp.feature_todolist.internal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.todo.TodoUseCases
import com.noteapp.core.state.UiState
import com.example.domain.model.Todo
import com.example.domain.model.TodoSortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListTodoViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases
) : ViewModel() {

    private var _todos: MutableStateFlow<UiState<List<Todo>>> = MutableStateFlow(UiState.Loading())
    val todos = _todos.asStateFlow()

    private var recentlyRemoved: Todo? = null
    private var todoSortOrder: TodoSortOrder = TodoSortOrder.DEFAULT
    private var jobObservingTodoList: Job? = null

    init {
        loadData()
    }

    private fun loadData(todoSortOrder: TodoSortOrder = TodoSortOrder.DEFAULT) {
        jobObservingTodoList?.cancel()
        jobObservingTodoList = viewModelScope.launch {
            todoUseCases.getAllTodos(todoSortOrder).distinctUntilChanged().collectLatest {
                _todos.value = UiState.Success(it)
            }
        }
    }

    fun onEvent(event: ListTodoEvent) = viewModelScope.launch {
        when (event) {
            is ListTodoEvent.ClearAll -> todoUseCases.deleteAllTodo()
            is ListTodoEvent.TryAgain -> loadData()
            is ListTodoEvent.UpdateTodoCompletedStatus -> todoUseCases.updateIsCompleted(
                event.todoId, event.isCompleted
            )
            is ListTodoEvent.DeleteItem -> {
                recentlyRemoved = event.todo
                todoUseCases.deleteTodo(event.todo.id)
            }
            ListTodoEvent.RestoreItem -> recentlyRemoved?.let { todoUseCases.addTodo(it) }
            is ListTodoEvent.UpdateSortOrder -> {
                if (todoSortOrder != event.todoSortOrder) {
                    todoSortOrder = event.todoSortOrder
                    loadData(todoSortOrder)
                }
            }

        }
    }

}
