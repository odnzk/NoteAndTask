package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "note_categories_table",
    primaryKeys = ["note_id", "category_id"]
)
data class NoteCategoryTable(
    @ColumnInfo(name = "note_id")
    val noteId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long
)
