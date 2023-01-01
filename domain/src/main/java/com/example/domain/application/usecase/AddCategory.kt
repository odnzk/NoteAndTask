package com.example.domain.application.usecase

import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import com.example.domain.util.exceptions.Field
import com.example.noteapp.ui.util.exceptions.InvalidCategoryException


class AddCategory(private val categoryRepository: CategoryRepository) {

    @Throws(InvalidCategoryException::class)
    suspend operator fun invoke(category: Category) {
        if (category.title.isNullOrBlank()) {
            throw InvalidCategoryException(Field.TITLE)
        }
        categoryRepository.add(category)
    }
}
