package com.noteapp.feature_detailedscreens.internal.navigation

import androidx.navigation.NavController
import com.noteapp.core.model.CategoryOwnerType
import com.noteapp.feature_detailedscreens.api.NoteDetailedFragmentDirections
import com.noteapp.feature_detailedscreens.api.TodoDetailedFragmentDirections

internal fun NavController.fromNoteToCategory(noteId: Long) {
    navigate(
        NoteDetailedFragmentDirections
            .actionNoteDetailFragmentToChooseCategoryDialog(
                CategoryOwnerType.NOTE_TYPE,
                noteId = noteId
            )
    )
}

internal fun NavController.fromTodoToCategory(todoId: Long) {
    navigate(
        TodoDetailedFragmentDirections
            .actionTodoDetailFragmentToChooseCategoryDialog(CategoryOwnerType.TODO_TYPE, todoId)
    )
}
