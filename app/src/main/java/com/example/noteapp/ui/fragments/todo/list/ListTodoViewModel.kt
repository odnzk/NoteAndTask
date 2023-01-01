package com.example.noteapp.ui.fragments.todo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private var _todos: MutableStateFlow<UiState<List<Todo>>> = MutableStateFlow(UiState.Loading())
    val todos: StateFlow<UiState<List<Todo>>> = _todos

    init {
        loadData()
    }

    private fun loadData() =
        viewModelScope.launch {
            todoRepository.getAll().collect {
                _todos.value = UiState.Success(it)
            }
        }


    // todo handle event (ListTodoEvent?)
    fun onEvent(event: ListTodoEvent) =
        viewModelScope.launch {
            when (event) {
                is ListTodoEvent.ClearAll -> todoRepository.deleteAll()
                is ListTodoEvent.TryAgain -> loadData()
                is ListTodoEvent.UpdateTodoCompletedStatus -> todoRepository.updateCompletedStatus(
                    event.todoId,
                    event.isCompleted
                )
            }
        }

}
