package com.example.noteapp.ui.dialogs.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.category.GetAllCategories
import com.example.domain.application.usecase.todo.AddTodo
import com.example.domain.model.Category
import com.example.domain.model.Todo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTodoViewModel @Inject constructor(
    private val getCategoriesUseCase: GetAllCategories, private val addTodo: AddTodo
) : ViewModel() {

    val currentTodo: Todo = Todo.defaultInstance()
    private var _categories = emptyList<Category>()
    val categories = _categories

    init {
        viewModelScope.launch {
            _categories = getCategoriesUseCase().first()
        }
    }

    fun addItem(todo: Todo) = viewModelScope.launch {
        addTodo(todo).fold(onSuccess = {
            // todo add notification worker is it needs
        }, onFailure = {
            // todo somehow show validation exception
        })
    }

}
