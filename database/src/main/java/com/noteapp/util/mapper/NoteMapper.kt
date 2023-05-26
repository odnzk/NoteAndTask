package com.noteapp.util.mapper

import com.noteapp.entity.tuples.NoteWithCategoriesTuple
import com.example.domain.model.Note
import com.noteapp.entity.NoteEntity

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
