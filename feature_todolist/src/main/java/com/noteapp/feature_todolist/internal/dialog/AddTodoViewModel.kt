package com.noteapp.feature_todolist.internal.dialog


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.example.domain.model.Category
import com.example.domain.model.Todo
import com.noteapp.core.state.CompletableState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    categoryUseCases: CategoryUseCases,
    private val todoUseCases: TodoUseCases
) : ViewModel() {

    private var _currentTodo: MutableStateFlow<CompletableState<Todo>> =
        MutableStateFlow(CompletableState.InProgress(Todo.defaultInstance()))
    val currentTodo = _currentTodo.asStateFlow()

    val categories: Flow<List<Category>> = categoryUseCases.getAllCategories()

    fun onEvent(event: AddTodoDialogEvent) = viewModelScope.launch {
        when (event) {
            AddTodoDialogEvent.AddTodo -> {
                todoUseCases.addTodo(currentTodo.value.data).fold(onSuccess = {
                    // todo add notification worker is it needs
                    _currentTodo.value = CompletableState.Completed(data = currentTodo.value.data)
                }, onFailure = { error ->
                    _currentTodo.value =
                        CompletableState.Error(data = currentTodo.value.data, error = error)
                })
            }
            is AddTodoDialogEvent.UpdateCategory -> {
                _currentTodo.update {
                    CompletableState.InProgress(data = it.data.copy(category = event.category))
                }
            }
            is AddTodoDialogEvent.UpdateDeadline -> {
                _currentTodo.update {
                    CompletableState.InProgress(data = it.data.copy(deadlineDate = event.date))
                }
            }
            is AddTodoDialogEvent.UpdateTitle -> {
                _currentTodo.update {
                    CompletableState.InProgress(data = it.data.copy(title = event.title))
                }
            }
            is AddTodoDialogEvent.UpdateReminder -> {
                _currentTodo.update {
                    CompletableState.InProgress(data = it.data.copy(reminderCalendar = event.calendar))
                }
            }
            is AddTodoDialogEvent.UpdatePeriodicity -> {} // todo
        }
    }

}
