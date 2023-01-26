package com.noteapp.feature_detailedscreens.internal.navigation

import androidx.navigation.NavController
import com.noteapp.core.model.CategoryOwnerType
import com.noteapp.feature_detailedscreens.api.NoteDetailedFragmentDirections
import com.noteapp.feature_detailedscreens.api.TodoDetailedFragmentDirections

internal fun NavController.fromNoteToChooseCategoryDialog(noteId: Long) =
    NoteDetailedFragmentDirections
        .actionNoteDetailFragmentToChooseCategoryDialog(
            type = CategoryOwnerType.NOTE_TYPE,
            noteId = noteId
        ).also { navigate(it) }


internal fun NavController.fromTodoToChooseCategoryDialog(todoId: Long) =
    TodoDetailedFragmentDirections.actionTodoDetailFragmentToChooseCategoryDialog(
        type = CategoryOwnerType.TODO_TYPE,
        todoId = todoId
    ).also { navigate(it) }

