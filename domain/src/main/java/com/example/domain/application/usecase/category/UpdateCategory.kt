package com.example.domain.application.usecase.category

import com.example.domain.model.Category
import com.example.domain.validation.CategoryValidator
import com.example.domain.repository.CategoryRepository
import com.example.noteapp.ui.util.exceptions.InvalidCategoryException

class UpdateCategory(
    private val categoryRepository: CategoryRepository,
    private val categoryValidator: CategoryValidator
) {

    @Throws(InvalidCategoryException::class)
    suspend operator fun invoke(category: Category) {
        if (categoryValidator.isValid(category)) {
            categoryRepository.update(category)
        }
    }
}
