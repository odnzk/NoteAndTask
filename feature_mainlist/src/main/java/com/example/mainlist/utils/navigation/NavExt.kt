package com.example.mainlist.utils.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.example.mainlist.list.ListFragmentDirections
import com.noteapp.ui.R


internal fun NavController.toDetailedNote(noteId: Long) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(
            context.getString(R.string.deeplink_detailed_note)
                .replace("{noteId}", noteId.toString())
        )
    ).build()
    navigate(deeplink)
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

internal fun NavController.toAddCategoryDialog() {
    navigate(ListFragmentDirections.actionListFragmentToAddCategoryDialog())
}

