package com.example.noteapp.ui.dialogs.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.GetAllCategories
import com.example.domain.application.usecase.todo.AddTodo
import com.example.domain.model.Category
import com.example.domain.model.Todo
import com.example.noteapp.ui.dialogs.CompletableState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTodoViewModel @Inject constructor(
    private val getCategoriesUseCase: GetAllCategories, private val addTodo: AddTodo
) : ViewModel() {

    private var _currentTodo: MutableStateFlow<CompletableState<Todo>> =
        MutableStateFlow(CompletableState.InProgress(Todo.defaultInstance()))
    val currentTodo = _currentTodo.asStateFlow()

    private var _categories = emptyList<Category>()
    val categories = _categories


    init {
        viewModelScope.launch {
            _categories = getCategoriesUseCase().first()
        }
    }

    fun onEvent(event: AddTodoDialogEvent) = viewModelScope.launch {
        when (event) {
            AddTodoDialogEvent.AddTodo -> {
                addTodo(currentTodo.value.data).fold(onSuccess = {
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
