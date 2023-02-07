package com.example.domain.application.usecase.category

import com.example.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class DeleteCategory(
    private val categoryRepository: CategoryRepository,
    private val dispatcher: CoroutineDispatcher
    ) {

    suspend operator fun invoke(categoryId: Long) = withContext(dispatcher) {
        categoryRepository.delete(categoryId)
    }
}
