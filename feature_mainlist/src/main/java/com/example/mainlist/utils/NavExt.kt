package com.example.mainlist.utils

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.mainlist.list.ListFragment
import com.example.mainlist.list.ListFragmentDirections
import com.noteapp.ui.R


internal fun Fragment.navigateToDetailedNote(noteId: Long) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(getString(R.string.deeplink_detailed_note).replace("{noteId}", noteId.toString()))
    ).build()
    findNavController().navigate(deeplink)
}

internal fun Fragment.navigateToDetailedTodo(todoId: Long) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(getString(R.string.deeplink_detailed_todo).replace("{todoId}", todoId.toString()))
    ).build()
    findNavController().navigate(deeplink)
}

internal fun ListFragment.navigateToAddCategoryDialog() =
    findNavController().navigate(ListFragmentDirections.actionListFragmentToAddCategoryDialog())

