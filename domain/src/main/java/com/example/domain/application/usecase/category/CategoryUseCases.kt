package com.example.domain.application.usecase.category

data class CategoryUseCases(
    val addCategory: AddCategory,
    val deleteCategory: DeleteCategory,
    val updateCategory: UpdateCategory,
    val getAllCategories: GetAllCategories
) {
}
