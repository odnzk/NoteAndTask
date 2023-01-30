package com.noteapp.repository

import com.example.data.mapper.toEntity
import com.example.data.mapper.toNote
import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.noteapp.dao.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override suspend fun add(elem: Note): Long = dao.insert(elem.toEntity())

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun update(elem: Note) {
        dao.update(elem.toEntity())
    }

    override suspend fun getById(id: Long): Note? =
        dao.getById(id)?.toNote()

    override fun getNoteFlowById(noteId: Long): Flow<Note?> =
        dao.getFlowById(noteId).map { it?.toNote() }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override fun getAll(): Flow<List<Note>> = dao.getAll()
        .map { list -> list.map { listItem -> listItem.toNote() } }

    override fun getByTitle(title: String): Flow<List<Note>> =
        dao.getByTitle(title)
            .map { list -> list.map { listItem -> listItem.toNote() } }

    override suspend fun addCategory(noteId: Long, categoryId: Long) {
        dao.insertNoteCategory(noteId, categoryId)
    }

    override suspend fun removeCategory(noteId: Long, categoryId: Long) {
        dao.removeNoteCategory(noteId, categoryId)
    }

    override fun getByCategoryId(categoryIds: Set<Long>, noteTitle: String): Flow<List<Note>> {
        return dao.getByCategoryId(categoryIds, noteTitle).map { list -> list.map { it.toNote() } }
    }
}
