package com.example.domain.model.validation

import com.example.domain.model.Todo
import com.example.domain.util.exceptions.Field
import com.example.noteapp.ui.util.exceptions.InvalidTodoException

class TodoValidator {

    @Throws(InvalidTodoException::class)
    fun isValid(todo: Todo): Boolean {
        if (todo.title.isBlank()) {
            throw InvalidTodoException(Field.TITLE)
        }
        return true
    }
}
