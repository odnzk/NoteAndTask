package com.noteapp.feature_todolist.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.CategoryUseCases
import com.example.domain.application.usecase.todo.TodoUseCases
import com.noteapp.core.state.CompletableState
import com.noteapp.model.Category
import com.noteapp.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val todoUseCases: TodoUseCases
) : ViewModel() {

    private var _currentTodo: MutableStateFlow<CompletableState<Todo>> =
        MutableStateFlow(CompletableState.InProgress(Todo.defaultInstance()))
    val currentTodo = _currentTodo.asStateFlow()

    val categories: Flow<List<Category>> = flow {
        emit(categoryUseCases.getAllCategories().first())
    }

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
            is AddTodoDialogEvent.UpdateCategory -> _currentTodo.value =
                CompletableState.InProgress(data = currentTodo.value.data.copy(category = event.category))
            is AddTodoDialogEvent.UpdateDeadlineDate -> _currentTodo.value =
                CompletableState.InProgress(data = currentTodo.value.data.copy(deadlineDate = event.date))
            is AddTodoDialogEvent.UpdateTitle -> _currentTodo.value =
                CompletableState.InProgress(data = currentTodo.value.data.copy(title = event.title))
            is AddTodoDialogEvent.UpdateReminderInfo -> _currentTodo.value =
                CompletableState.InProgress(
                    data = currentTodo.value.data.copy(
                        notificationCalendar = event.calendar
                    )
                )
            is AddTodoDialogEvent.UpdatePeriodInfo -> TODO()
        }
    }

}
