package com.example.domain.validation

import com.example.domain.model.Todo
import com.example.noteapp.ui.util.exceptions.InvalidTodoException

class TodoValidator {

    fun isValid(todo: Todo): Result<Boolean> =
        if (todo.title.isBlank() || todo.title.length !in MIN_LENGTH..MAX_LENGTH) {
            Result.failure(InvalidTodoException(Field.TITLE))
        } else Result.success(true)


    companion object {
        const val MAX_LENGTH = 40
        const val MIN_LENGTH = 2
    }
}
