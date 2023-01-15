package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "note_categories_table",
    primaryKeys = ["note_id", "category_id"]
//    foreignKeys = [
//        ForeignKey(
//            entity = NoteEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["note_id"],
//            onDelete = ForeignKey.CASCADE,
//            onUpdate = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = CategoryEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["category_id"],
//            onDelete = ForeignKey.CASCADE,
//            onUpdate = ForeignKey.CASCADE,
//        )
)
data class NoteCategoryTable(
    @ColumnInfo(name = "note_id")
    val noteId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long
)
