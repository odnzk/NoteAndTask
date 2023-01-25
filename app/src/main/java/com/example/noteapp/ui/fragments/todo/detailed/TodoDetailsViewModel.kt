package com.example.noteapp.ui.fragments.todo.detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import com.noteapp.model.Todo
import com.noteapp.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailsViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
    private val state: SavedStateHandle
) : ViewModel() {

    val todoId: Long by lazy {
        state.get<Long>("todoId") ?: throw InvalidNavArgumentsException()
    }
    private var _todo: MutableStateFlow<UiState<Todo>> = MutableStateFlow(UiState.Loading())
    val todo = _todo.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _todo.value = UiState.Loading()
        viewModelScope.launch {
            todoUseCases.getTodoById(todoId).fold(
                onSuccess = { _todo.value = UiState.Success(it) },
                onFailure = { _todo.value = UiState.Error(it) }
            )
        }
    }

    fun onEvent(event: TodoDetailedEvent) = viewModelScope.launch {
        when (event) {
            is TodoDetailedEvent.UpdateTodo -> {
                todoUseCases.updateTodo(event.todo).onFailure {
                    _todo.value = UiState.Error(it)
                }
            }
            is TodoDetailedEvent.DeleteTodo -> {
                // if UiState.Loading or UiState.Error do nothing
                _todo.value.data?.let {
                    todoUseCases.deleteTodo(it.id)
                }
            }
            is TodoDetailedEvent.TryLoadingTodoAgain -> loadData()
        }
    }

}
