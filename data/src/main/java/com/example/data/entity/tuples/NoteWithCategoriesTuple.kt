package com.example.data.entity.tuples

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.data.entity.NoteCategoryTable
import com.example.data.entity.NoteEntity
import com.example.domain.model.Category

data class NoteWithCategoriesTuple(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id",
        associateBy = Junction(NoteCategoryTable::class)
    )
    val categories: List<Category>
)
