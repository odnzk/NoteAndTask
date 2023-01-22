package com.example.domain.validation

import com.example.domain.model.Category
import com.example.domain.util.exceptions.Field
import com.example.noteapp.ui.util.exceptions.InvalidCategoryException

class CategoryValidator {

    @Throws(InvalidCategoryException::class)
    fun isValid(category: Category): Boolean {
        if (category.title.isBlank()) {
            throw InvalidCategoryException(Field.TITLE)
        }
        // todo check color validity
        return true
    }
}
