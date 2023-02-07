package com.example.domain.application.usecase.category

import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn


class GetAllCategories(
    private val categoryRepository: CategoryRepository,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(): Flow<List<Category>> = categoryRepository.getAll().flowOn(dispatcher)
}
