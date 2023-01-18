package com.example.data.mapper

import com.example.data.entity.CategoryEntity
import com.example.domain.model.Category

class CategoryMapper {
    fun toCategory(categoryEntity: CategoryEntity): Category =
        Category(categoryEntity.id, categoryEntity.title, categoryEntity.color)

    fun from(category: Category): CategoryEntity {
        return CategoryEntity(
            title = category.title,
            color = category.color
        )
    }
}
