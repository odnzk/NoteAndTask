package com.example.domain.application.usecase.category

import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import com.example.domain.validation.CategoryValidator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateCategory(
    private val categoryRepository: CategoryRepository,
    private val categoryValidator: CategoryValidator,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(category: Category): Result<Int> = withContext(dispatcher) {
        categoryValidator.hasException(category)?.let { Result.failure(it) }
            ?: categoryRepository.update(category)
    }
}
