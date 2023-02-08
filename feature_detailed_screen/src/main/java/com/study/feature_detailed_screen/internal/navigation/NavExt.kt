package com.study.feature_detailed_screen.internal.navigation

import androidx.navigation.NavController
import com.noteapp.core.model.CategoryOwnerType
import com.study.feature_detailed_screen.api.NoteDetailedFragmentDirections
import com.study.feature_detailed_screen.api.TodoDetailedFragmentDirections

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

