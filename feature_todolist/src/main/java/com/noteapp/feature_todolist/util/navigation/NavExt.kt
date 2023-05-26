package com.noteapp.feature_todolist.util.navigation

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.noteapp.feature_todolist.list.TodosListFragment
import com.noteapp.feature_todolist.list.TodosListFragmentDirections
import com.noteapp.ui.R

internal fun TodosListFragment.navigateToAddTodoBottomSheetDialog() {
    findNavController()
        .navigate(TodosListFragmentDirections.actionTodosListFragmentToAddTodoBottomSheetDialog())
}

internal fun TodosListFragment.navigateToTodoFiltersDialog() {
    findNavController()
        .navigate(TodosListFragmentDirections.actionTodosListFragmentToFiltersTodoBottomSheetDialog())
}

internal fun Fragment.navigateToDetailedTodo(todoId: Long) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(getString(R.string.deeplink_detailed_todo).replace("{todoId}", todoId.toString()))
    ).build()
    findNavController().navigate(deeplink)
}
