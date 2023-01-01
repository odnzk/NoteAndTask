package com.example.domain.application.usecase.category

import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository


class DeleteCategory(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(category: Category) {
        categoryRepository.delete(category)
    }
}
