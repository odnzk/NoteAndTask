package com.noteapp.feature_todolist.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.noteapp.feature_todolist.list.TodosListFragmentDirections
import com.noteapp.ui.R

internal fun NavController.toAddTodoBottomSheetDialog() {
    navigate(TodosListFragmentDirections.actionTodosListFragmentToAddTodoBottomSheetDialog())
}

internal fun NavController.toDetailedTodo(todoId: Long) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(
            context.getString(R.string.deeplink_detailed_todo)
                .replace("{todoId}", todoId.toString())
        )
    ).build()
    navigate(deeplink)
}
