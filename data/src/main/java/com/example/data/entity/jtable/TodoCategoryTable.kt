package com.example.data.entity.jtable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "todo_categories_table",
    primaryKeys = ["todo_id", "category_id"],
)
data class TodoCategoryTable(
    @ColumnInfo(name = "todo_id")
    val todoId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long
)
