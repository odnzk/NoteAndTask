package com.example.domain.repository

import com.example.domain.model.Note
import com.example.domain.util.Filter
import com.example.domain.util.SortOrder
import kotlinx.coroutines.flow.Flow


interface NoteRepository {
    suspend fun add(note: Note): Long

    suspend fun delete(note: Note)

    suspend fun update(note: Note)

    suspend fun getById(id: Long): Note?

    suspend fun deleteAll()

    fun getAll(searchQuery: String, sortOrder: SortOrder, filter: Filter): Flow<List<Note>>

    fun getAll(): Flow<List<Note>>
}
