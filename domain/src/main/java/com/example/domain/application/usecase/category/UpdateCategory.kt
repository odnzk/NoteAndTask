package com.example.domain.application.usecase.category

import com.example.domain.repository.CategoryRepository
import com.example.domain.validation.CategoryValidator
import com.noteapp.model.Category

class UpdateCategory(
    private val categoryRepository: CategoryRepository,
    private val categoryValidator: CategoryValidator
) {

    suspend operator fun invoke(category: Category): Result<Boolean> {
        val result: Result<Boolean> = categoryValidator.isValid(category)
        return if (result.isSuccess) {
            categoryRepository.update(category)
            Result.success(true)
        } else result
    }
}
