package com.noteapp.feature_todolist.navigation

import androidx.navigation.NavController
import com.noteapp.feature_todolist.list.TodosListFragmentDirections

internal fun NavController.toAddTodoBottomSheetDialog() =
    navigate(TodosListFragmentDirections.actionTodosListFragmentToAddTodoBottomSheetDialog())

internal fun NavController.toDetailedTodo(todoId: Long) {
    navigate(TodosListFragmentDirections.actionTodosListFragmentToTodoDetailFragment(todoId))
}
