package com.noteapp.feature_todolist.navigation

import androidx.navigation.NavController
import com.noteapp.feature_todolist.list.TodosListFragmentDirections

//  findNavController().navigate(R.id.action_todosListFragment_to_addTodoBottomSheetDialog)
internal fun NavController.toAddTodoBottomSheetDialog() =
    navigate(TodosListFragmentDirections.actionTodosListFragmentToAddTodoBottomSheetDialog())

internal fun NavController.toDetailedTodo(todoId: Long) {
    // todo
//    navigate(TodosListFragmentDirections.actionTodosListFragmentToTodoDetailFragment(todoId))
}
