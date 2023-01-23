package com.example.domain.application.usecase.category

import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import com.example.domain.validation.CategoryValidator


class AddCategory(
    private val categoryRepository: CategoryRepository,
    private val categoryValidator: CategoryValidator
) {
    suspend operator fun invoke(category: Category): Result<Long> {
        val result = categoryValidator.isValid(category)
        return result.exceptionOrNull()?.let { Result.failure(it) } ?: Result.success(
            categoryRepository.add(category)
        )
    }
}
