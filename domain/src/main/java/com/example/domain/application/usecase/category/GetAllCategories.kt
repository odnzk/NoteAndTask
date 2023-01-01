package com.example.domain.application.usecase.category

import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow


class GetAllCategories(private val categoryRepository: CategoryRepository) {

    operator fun invoke(): Flow<List<Category>> = categoryRepository.getAll()
}
