package com.example.domain.repository

import com.noteapp.model.Note
import kotlinx.coroutines.flow.Flow


interface NoteRepository : BasicRepository<Note> {

    suspend fun deleteAll()

    fun getByTitle(title: String): Flow<List<Note>>

    suspend fun addCategory(noteId: Long, categoryId: Long)

    suspend fun removeCategory(noteId: Long, categoryId: Long)

    fun getByCategoryId(categoryId: Long, noteTitle: String): Flow<List<Note>>
}
