package com.example.data.mapper

import com.example.data.entity.CategoryEntity
import com.noteapp.model.Category

internal fun CategoryEntity.toCategory() = Category(id, title, color)
internal fun Category.toEntity() = CategoryEntity(title = title, color = color)
