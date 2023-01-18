package com.example.data.mapper

import com.example.data.entity.CategoryEntity
import com.example.domain.model.Category

fun CategoryEntity.toCategory() = Category(id, title, color)
fun Category.toEntity() = CategoryEntity(title = title, color = color)
