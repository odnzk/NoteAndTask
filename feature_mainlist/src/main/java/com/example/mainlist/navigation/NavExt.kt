package com.example.mainlist.navigation

import androidx.navigation.NavController
import com.example.mainlist.list.ListFragmentDirections


internal fun NavController.toDetailedNote(noteId: Long) {
// todo
}

internal fun NavController.toDetailedTodo(todoId: Long) {
// todo
}

internal fun NavController.toAddCategoryDialog() =
    navigate(ListFragmentDirections.actionListFragmentToAddCategoryDialog())
