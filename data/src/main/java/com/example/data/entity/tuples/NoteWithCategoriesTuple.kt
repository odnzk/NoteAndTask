package com.example.data.entity.tuples

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.data.entity.CategoryEntity
import com.example.data.entity.NoteCategoryTable
import com.example.data.entity.NoteEntity
import com.example.domain.model.Note

data class NoteWithCategoriesTuple(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "note_id",
        entityColumn = "category_id",
        associateBy = Junction(NoteCategoryTable::class))
    val categories: List<CategoryEntity>
) {
    fun toNote(): Note = Note(
        id = note.id,
        title = note.title,
        content = note.content,
        date = note.date,
        categories = categories.map { list -> list.toCategory() },
    )
}
