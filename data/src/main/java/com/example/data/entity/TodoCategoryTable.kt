package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "todo_categories_table",
    foreignKeys = [
        ForeignKey(
            entity = TodoEntity::class,
            parentColumns = ["id"],
            childColumns = ["todo_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    primaryKeys = ["todo_id", "category_id"],
    indices = [
        Index("todo_id"),
        Index("category_id")
    ]
)
data class TodoCategoryTable(
    @ColumnInfo(name = "todo_id")
    val todoId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long
)
