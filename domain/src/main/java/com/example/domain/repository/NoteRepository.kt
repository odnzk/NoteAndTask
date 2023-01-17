package com.example.domain.repository

import com.example.domain.model.Note
import kotlinx.coroutines.flow.Flow


interface NoteRepository {
    suspend fun add(note: Note): Long

    suspend fun delete(note: Note)

    suspend fun update(note: Note)

    suspend fun deleteAll()

    suspend fun getById(id: Long): Note?

    fun getAll(): Flow<List<Note>>

    fun getByTitle(title: String): Flow<List<Note>>

    suspend fun addCategory(noteId: Long, categoryId: Long)

    suspend fun removeCategory(noteId: Long, categoryId: Long)
}
