package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String, val color: Int
) {
    fun mapToCategory(): Category =
        Category(title, color)

    companion object{
        fun toCategoryEntity(category: Category): CategoryEntity {
            return CategoryEntity(
                title = category.title,
                color = category.color
            )
        }
    }
}

