package com.example.domain.validation

import com.example.domain.model.Todo
import com.example.domain.util.exceptions.Field
import com.example.noteapp.ui.util.exceptions.InvalidTodoException

class TodoValidator {

    fun isValid(todo: Todo): Result<Boolean> =
        if (todo.title.isBlank()) {
            Result.failure(InvalidTodoException(Field.TITLE))
        } else Result.success(true)

}
