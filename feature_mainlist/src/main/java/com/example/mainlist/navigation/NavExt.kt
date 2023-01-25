package com.example.mainlist.navigation

import androidx.navigation.NavController
import com.example.mainlist.list.ListFragmentDirections


internal fun NavController.toDetailedNote(noteId: Long) {
    navigate(ListFragmentDirections.actionListFragmentToNoteDetailFragment(noteId))
}

internal fun NavController.toDetailedTodo(todoId: Long) {
    navigate(ListFragmentDirections.actionListFragmentToTodoDetailFragment(todoId))
}

internal fun NavController.toAddCategoryDialog() =
    navigate(ListFragmentDirections.actionListFragmentToAddCategoryDialog())
