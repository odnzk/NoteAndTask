package com.example.data.repository

import com.example.data.dao.NoteDao
import com.example.data.entity.NoteEntity
import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override suspend fun add(note: Note): Long = dao.insert(NoteEntity.from(note))

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }

    override suspend fun update(note: Note) {
        dao.update(NoteEntity.from(note))
    }

    override suspend fun getById(id: Long): Note? = dao.getById(id)?.toNote()

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override fun getAll(): Flow<List<Note>> = dao.getAll().map { list -> list.map { it.toNote() } }

    override fun getByTitle(title: String): Flow<List<Note>> =
        dao.getByTitle(title).map { list -> list.map { it.toNote() } }

    override suspend fun addCategory(noteId: Long, categoryId: Long) {
        dao.insertNoteCategory(noteId, categoryId)
    }

    override suspend fun removeCategory(noteId: Long, categoryId: Long) {
        dao.removeNoteCategory(noteId, categoryId)
    }
}
