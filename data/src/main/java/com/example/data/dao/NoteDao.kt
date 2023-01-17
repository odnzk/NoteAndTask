package com.example.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.data.entity.NoteEntity
import com.example.data.entity.tuples.NoteWithCategoriesTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM NOTES")
    fun getAll(): Flow<List<NoteWithCategoriesTuple>>

    @Transaction
    @Query("SELECT * FROM NOTES WHERE note_id = :noteId")
    suspend fun getById(noteId: Long): NoteWithCategoriesTuple?

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :noteTitle || '%'")
    fun getByTitle(noteTitle: String): Flow<List<NoteWithCategoriesTuple>>


    @Insert(onConflict = REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Query("DELETE FROM notes WHERE note_id = :noteId")
    suspend fun delete(noteId: Long)

    @Update(onConflict = REPLACE)
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Query("delete from note_categories_table where note_id = :noteId AND category_id =:categoryId")
    suspend fun removeNoteCategory(noteId: Long, categoryId: Long)

    @Query("insert into note_categories_table values (:noteId, :categoryId)")
    suspend fun insertNoteCategory(noteId: Long, categoryId: Long)

}
