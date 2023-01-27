package com.example.domain.repository

import com.example.domain.model.Note
import kotlinx.coroutines.flow.Flow


interface NoteRepository : BasicRepository<Note> {

    fun getNoteFlowById(noteId: Long): Flow<Note?>

    suspend fun deleteAll()

    fun getByTitle(title: String): Flow<List<Note>>

    suspend fun addCategory(noteId: Long, categoryId: Long)

    suspend fun removeCategory(noteId: Long, categoryId: Long)

    fun getByCategoryId(categoryIds: Set<Long>, noteTitle: String = ""): Flow<List<Note>>
}
