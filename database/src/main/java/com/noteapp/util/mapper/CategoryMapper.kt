package com.noteapp.util.mapper

import com.example.data.entity.CategoryEntity
import com.example.domain.model.Category

internal fun CategoryEntity.toCategory() = Category(id, title, color)
internal fun Category.toEntity() = CategoryEntity(title = title, color = color)
