package com.example.domain.application.usecase.category

import com.example.domain.model.Category
import com.example.domain.model.validation.CategoryValidator
import com.example.domain.repository.CategoryRepository
import com.example.noteapp.ui.util.exceptions.InvalidCategoryException


class AddCategory(private val categoryRepository: CategoryRepository) {

    @Throws(InvalidCategoryException::class)
    suspend operator fun invoke(category: Category) {
        if (CategoryValidator().isValid(category)) {
            categoryRepository.add(category)
        }
    }
}
