package com.example.domain.repository

import com.example.domain.model.Filter
import com.example.domain.model.Note
import com.example.domain.model.SortOrder
import kotlinx.coroutines.flow.Flow


interface NoteRepository {
    suspend fun add(note: Note): Long

    suspend fun delete(note: Note)

    suspend fun update(note: Note)

    suspend fun deleteAll()

    suspend fun getById(id: Long): Note?

    fun getByTitle(title: String): Flow<List<Note>>

    fun getAll(searchQuery: String, sortOrder: SortOrder, filter: Filter): Flow<List<Note>>

    suspend fun addCategory(noteId: Long, categoryId: Long)

    suspend fun removeCategory(noteId: Long, categoryId: Long)
}
