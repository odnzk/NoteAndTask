package com.example.domain.validation

import com.example.noteapp.ui.util.exceptions.InvalidTodoException
import com.example.domain.model.Todo

class TodoValidator {

    fun isValid(todo: Todo): Result<Boolean> =
        if (todo.title.isBlank()) {
            Result.failure(InvalidTodoException(Field.TITLE))
        } else Result.success(true)

}
