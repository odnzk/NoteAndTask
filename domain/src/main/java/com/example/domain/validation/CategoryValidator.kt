package com.example.domain.validation

import com.example.noteapp.ui.util.exceptions.InvalidCategoryException
import com.noteapp.model.Category

class CategoryValidator {

    fun isValid(category: Category): Result<Boolean> {
        return if (category.title.isBlank()) {
            Result.failure(InvalidCategoryException(Field.TITLE))
        } else Result.success(true)
    }
}
