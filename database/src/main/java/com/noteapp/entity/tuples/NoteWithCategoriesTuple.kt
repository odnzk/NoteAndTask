package com.example.data.tuples

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.data.entity.CategoryEntity
import com.example.data.entity.jtable.NoteCategoryTable
import com.example.data.entity.NoteEntity

data class NoteWithCategoriesTuple(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "note_id",
        entityColumn = "category_id",
        associateBy = Junction(NoteCategoryTable::class))
    val categories: List<CategoryEntity>
)
// one note - many categories
