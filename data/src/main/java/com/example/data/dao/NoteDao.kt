package com.example.data.dao

import androidx.room.*
import com.example.data.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<NoteEntity>>

    @Update
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): NoteEntity?

    // todo
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%'")
    suspend fun getNoteByTitle(searchQuery: String)

}
