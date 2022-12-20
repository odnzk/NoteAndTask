package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val color: String
) {
    fun mapToCategory(): Category =
        Category(title, color)

    companion object{
        fun from(category: Category): CategoryEntity {
            return CategoryEntity(
                title = category.title,
                color = category.color
            )
        }
    }
}

