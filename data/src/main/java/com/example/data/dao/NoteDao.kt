package com.example.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.data.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<NoteEntity>>

    @Update(onConflict = REPLACE)
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): NoteEntity?

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%'")
    suspend fun getNoteByTitle(searchQuery: String): List<NoteEntity>

    @Query("delete from note_categories_table where note_id = :noteId AND category_id =:categoryId")
    suspend fun removeNoteCategory(noteId: Long, categoryId: Long)

    @Query("insert into note_categories_table values (:noteId, :categoryId)")
    suspend fun insertNoteCategory(noteId: Long, categoryId: Long)
}
