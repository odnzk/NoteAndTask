package com.noteapp.util.mapper

import com.example.domain.model.Category
import com.noteapp.entity.CategoryEntity

internal fun CategoryEntity.toCategory() = Category(id, title, color)
internal fun Category.toEntity() = CategoryEntity(title = title, color = color)
