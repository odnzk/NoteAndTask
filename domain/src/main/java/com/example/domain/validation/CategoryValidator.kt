package com.example.domain.validation

import com.example.domain.model.Category
import com.example.domain.util.Field
import com.example.noteapp.ui.util.exceptions.InvalidCategoryException

class CategoryValidator {

    fun isValid(category: Category): Result<Boolean> {
        return if (category.title.isBlank()) {
            Result.failure(InvalidCategoryException(Field.TITLE))
        } else Result.success(true)
    }
}
