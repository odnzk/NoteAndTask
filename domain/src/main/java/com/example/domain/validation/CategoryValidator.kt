package com.example.domain.validation

import com.example.domain.model.Category
import com.example.noteapp.ui.util.exceptions.InvalidCategoryException

class CategoryValidator {

    fun isValid(category: Category): Result<Boolean> {
        return if (category.title.isBlank()) {
            Result.failure(InvalidCategoryException(Field.TITLE))
        } else Result.success(true)
    }

    companion object {
        const val MAX_LENGTH = 20
        const val MIN_LENGTH = 2
    }
}
