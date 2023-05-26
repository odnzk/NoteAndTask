package com.example.domain.validation

import com.example.domain.model.Category
import com.example.domain.util.exceptions.Field
import com.example.domain.util.exceptions.InvalidCategoryException

class CategoryValidator {
    fun hasException(category: Category): Exception? {
        return if (category.title.isBlank() || category.title.length !in MIN_LENGTH..MAX_LENGTH) {
            InvalidCategoryException(Field.TITLE)
        } else null
    }

    companion object {
        const val MAX_LENGTH = 20
        const val MIN_LENGTH = 2
    }
}
