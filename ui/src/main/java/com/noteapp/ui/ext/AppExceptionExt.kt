package com.noteapp.ui.ext

import android.content.Context
import com.example.domain.util.exceptions.*
import com.noteapp.ui.R

fun Context.handleException(throwable: Throwable): HandledError {
    val message = when (throwable) {
        is NotFoundException -> getString(R.string.error_not_found)
        is LoadingFromDatabaseFailedException -> getString(R.string.error_failed_loading_from_database)
        is InvalidNavArgumentsException -> getString(R.string.error_navigation_exception)
        is InvalidCategoryException -> getString(R.string.error_invalid_category_title)
        is InvalidTodoException -> getString(R.string.error_invalid_todo_title)
        is InvalidNoteException -> getString(
            if (throwable.field == Field.TITLE)
                R.string.error_invalid_note_title else R.string.error_invalid_note_content
        )
        is NotUniqueFieldException ->
            when (throwable.constraint) {
                UniqueConstraints.CATEGORY_TITLE -> getString(R.string.error_category_with_this_title_already_exist)
                UniqueConstraints.NOTE_TITLE -> getString(R.string.error_note_with_this_title_already_exist)
                UniqueConstraints.TODO_TITLE -> getString(R.string.error_todo_with_this_title_already_exist)
            }
        else -> getString(R.string.error_unknown, throwable.message.orEmpty())
    }
    return HandledError(throwable, message)
}


data class HandledError(val error: Throwable, val message: String) {
    constructor(error: Throwable) : this(error, error.message.orEmpty())
}
