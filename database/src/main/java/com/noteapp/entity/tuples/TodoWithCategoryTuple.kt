package com.noteapp.entity.tuples

import androidx.room.Embedded
import androidx.room.Relation
import com.noteapp.entity.CategoryEntity
import com.noteapp.entity.TodoEntity

data class TodoWithCategoryTuple(
    @Embedded val todo: TodoEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val category: CategoryEntity? = null
)
