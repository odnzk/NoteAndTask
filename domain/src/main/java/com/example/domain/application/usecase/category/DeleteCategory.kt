package com.example.domain.application.usecase.category

import com.example.domain.repository.CategoryRepository


class DeleteCategory(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(categoryId: Long) {
        categoryRepository.delete(categoryId)
    }
}
