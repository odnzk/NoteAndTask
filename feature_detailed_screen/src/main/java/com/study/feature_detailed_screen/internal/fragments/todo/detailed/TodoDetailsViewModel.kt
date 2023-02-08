package com.study.feature_detailed_screen.internal.fragments.todo.detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.Todo
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import com.example.noteapp.ui.util.exceptions.NotFoundException
import com.noteapp.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TodoDetailsViewModel @Inject constructor(
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
        viewModelScope.launch {
            todoUseCases.getTodoFlowById(todoId).distinctUntilChanged().collectLatest { todo ->
                _todo.update {
                    todo?.let { UiState.Success(todo) } ?: UiState.Error(NotFoundException())
                }
            }
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
            is TodoDetailedEvent.Reload -> loadData()
        }
    }

}
