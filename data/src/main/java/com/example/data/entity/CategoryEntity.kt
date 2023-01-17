package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val id: Long = 0,
    val title: String,
    val color: Int
) {
    fun toCategory(): Category =
        Category(id, title, color)

    companion object{
        fun from(category: Category): CategoryEntity {
            return CategoryEntity(
                title = category.title,
                color = category.color
            )
        }
    }
}

