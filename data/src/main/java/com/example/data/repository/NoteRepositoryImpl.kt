package com.example.data.repository

import com.example.data.dao.NoteDao
import com.example.data.entity.NoteEntity
import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.domain.util.Filter
import com.example.domain.util.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override suspend fun add(note: Note): Long {
        return dao.insert(NoteEntity.from(note))
    }

    override suspend fun delete(note: Note) {
        dao.delete(NoteEntity.from(note))
    }

    override suspend fun update(note: Note) {
        dao.update(NoteEntity.from(note))
    }

    override suspend fun getById(id: Long): Note? {
        return dao.findById(id)?.mapToNote()
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override fun getAll(
        searchQuery: String,
        sortOrder: SortOrder,
        filter: Filter
    ): Flow<List<Note>> {
        return dao.getAll().map {
            it.map { noteEntity -> noteEntity.mapToNote() } // todo
        }
    }

    override fun getAll(): Flow<List<Note>> {
        return dao.getAll().map {
            it.map { noteEntity -> noteEntity.mapToNote() }
        }
    }

    override suspend fun addCategory(noteId: Long, categoryId: Long) {
        dao.insertNoteCategory(noteId, categoryId)
    }

    override suspend fun removeCategory(noteId: Long, categoryId: Long) {
        dao.removeNoteCategory(noteId, categoryId)
    }
}
