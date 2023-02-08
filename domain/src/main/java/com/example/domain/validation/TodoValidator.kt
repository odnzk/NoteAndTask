package com.example.domain.validation

import com.example.domain.model.Todo
import com.example.domain.util.exceptions.Field
import com.example.domain.util.exceptions.InvalidTodoException

class TodoValidator {

    fun hasException(todo: Todo): Exception? =
        if (todo.title.isBlank() || todo.title.length !in MIN_LENGTH..MAX_LENGTH) {
            InvalidTodoException(Field.TITLE)
        } else null


    companion object {
        const val MAX_LENGTH = 40
        const val MIN_LENGTH = 2
    }
}
