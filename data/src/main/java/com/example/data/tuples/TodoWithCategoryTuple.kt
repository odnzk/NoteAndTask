package com.example.data.tuples

import androidx.room.Embedded
import androidx.room.Relation
import com.example.data.entity.CategoryEntity
import com.example.data.entity.TodoEntity
import com.example.domain.model.Category

data class TodoWithCategoryTuple(
    @Embedded val todo: TodoEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val category: CategoryEntity? = null
)
// one task - one category
