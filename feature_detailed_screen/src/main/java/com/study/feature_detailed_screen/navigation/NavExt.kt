package com.study.feature_detailed_screen.navigation

import androidx.navigation.fragment.findNavController
import com.noteapp.core.model.CategoryOwnerType
import com.study.feature_detailed_screen.fragments.note.detailed.NoteDetailedFragment
import com.study.feature_detailed_screen.fragments.note.detailed.NoteDetailedFragmentDirections
import com.study.feature_detailed_screen.fragments.todo.detailed.TodoDetailedFragment
import com.study.feature_detailed_screen.fragments.todo.detailed.TodoDetailedFragmentDirections

internal fun NoteDetailedFragment.navigateToChooseCategoryDialog(noteId: Long) =
    NoteDetailedFragmentDirections
        .actionNoteDetailFragmentToChooseCategoryDialog(
            type = CategoryOwnerType.NOTE_TYPE,
            noteId = noteId
        ).also { findNavController().navigate(it) }


internal fun TodoDetailedFragment.navigateToChooseCategoryDialog(todoId: Long) =
    TodoDetailedFragmentDirections.actionTodoDetailFragmentToChooseCategoryDialog(
        type = CategoryOwnerType.TODO_TYPE,
        todoId = todoId
    ).also { findNavController().navigate(it) }

