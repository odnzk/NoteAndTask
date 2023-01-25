package com.example.data.mapper

import com.example.data.entity.NoteEntity
import com.example.data.tuples.NoteWithCategoriesTuple
import com.noteapp.model.Note

internal fun Note.toEntity() = NoteEntity(
    id = id, title = title, content = content, date = date
)

internal fun NoteWithCategoriesTuple.toNote() = Note(id = note.id,
    title = note.title,
    content = note.content,
    date = note.date,
    categories = categories.map { categoryEntity ->
        categoryEntity.toCategory()
    })