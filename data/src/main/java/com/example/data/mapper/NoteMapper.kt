package com.example.data.mapper

import com.example.data.entity.NoteEntity
import com.example.data.tuples.NoteWithCategoriesTuple
import com.example.domain.model.Note

class NoteMapper(private val categoryMapper: CategoryMapper) {
    fun from(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id, title = note.title, content = note.content, date = note.date
        )
    }

    fun toNote(tuple: NoteWithCategoriesTuple): Note =
        Note(id = tuple.note.id,
            title = tuple.note.title,
            content = tuple.note.content,
            date = tuple.note.date,
            categories = tuple.categories.map { categoryEntity ->
                categoryMapper.toCategory(
                    categoryEntity
                )
            })
}
