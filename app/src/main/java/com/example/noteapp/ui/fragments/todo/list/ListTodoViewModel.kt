package com.example.noteapp.ui.fragments.todo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.Category
import com.example.domain.model.Todo
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListTodoViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases, private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private var _todos: MutableStateFlow<UiState<List<Todo>>> = MutableStateFlow(UiState.Loading())
    val todos: StateFlow<UiState<List<Todo>>> = _todos

    private var _categories: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())
    val categories = _categories.asStateFlow()

    init {
        loadData()
        initCategories()
    }

    private fun initCategories() = viewModelScope.launch {
        categoryUseCases.getAllCategories().collectLatest {
            _categories.value = it
        }
    }

    private fun loadData() = viewModelScope.launch {
        todoUseCases.getAllTodos().collectLatest {
            _todos.value = UiState.Success(it)
        }
    }

    fun onEvent(event: ListTodoEvent) = viewModelScope.launch {
        when (event) {
            is ListTodoEvent.ClearAll -> todoUseCases.deleteAllTodo()
            is ListTodoEvent.TryAgain -> loadData()
            is ListTodoEvent.UpdateTodoCompletedStatus -> todoUseCases.updateIsCompleted(
                event.todoId, event.isCompleted
            )
            is ListTodoEvent.AddItem -> todoUseCases.addTodo(event.todo)
        }
    }

}
