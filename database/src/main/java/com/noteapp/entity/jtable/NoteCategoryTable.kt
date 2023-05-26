package com.noteapp.entity.jtable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "note_categories_table",
    primaryKeys = ["note_id", "category_id"],
    indices = [Index(value = ["note_id", "category_id"], unique = true)]
)
data class NoteCategoryTable(
    @ColumnInfo(name = "note_id")
    val noteId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long
)
