package com.example.domain.validation

import com.example.noteapp.ui.util.exceptions.InvalidTodoException
import com.noteapp.model.Todo

class TodoValidator {

    fun isValid(todo: Todo): Result<Boolean> =
        if (todo.title.isBlank()) {
            Result.failure(InvalidTodoException(Field.TITLE))
        } else Result.success(true)

}
