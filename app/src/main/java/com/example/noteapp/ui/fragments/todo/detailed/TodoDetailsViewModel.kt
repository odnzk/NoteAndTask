package com.example.noteapp.ui.fragments.todo.detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.Todo
import com.example.noteapp.ui.util.UiState
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailsViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
    private val state: SavedStateHandle
) : ViewModel() {

    private val todoId: Long? by lazy { state.get<Long>("todoId") }
    private var _todo: MutableStateFlow<UiState<Todo>> = MutableStateFlow(UiState.Loading())
    val todo: StateFlow<UiState<Todo>> = _todo.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _todo.value = UiState.Loading()
        viewModelScope.launch {
            todoId?.let {
                // todo handle exception in getTodoById(it)
                val repoTodo = todoUseCases.getTodoById(it)
                _todo.value = UiState.Success(repoTodo)
            } ?: run { _todo.value = UiState.Error(InvalidNavArgumentsException()) }
        }
    }

    fun onEvent(event: TodoDetailedEvent) = viewModelScope.launch {
        when (event) {
            is TodoDetailedEvent.UpdateTodo -> {
                todoUseCases.updateTodo(event.todo)
            }
            is TodoDetailedEvent.DeleteTodo -> {
                // if UiState.Loading or UiState.Error do nothing
                todo.value.data?.let {
                    todoUseCases.deleteTodo(it)
                }
            }
            is TodoDetailedEvent.TryLoadingTodoAgain -> loadData()
        }
    }

}
