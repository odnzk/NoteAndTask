package com.example.data.dao

import androidx.room.*
import com.example.data.entity.CategoryEntity
import com.example.data.entity.tuples.NoteWithCategoriesTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteById(id: Long?)

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Long): CategoryEntity?

    @Query("SELECT * FROM categories")
    fun getAll(): Flow<List<CategoryEntity>>


    @Transaction
    @Query("SELECT * FROM NOTES WHERE id = :noteId")
    suspend fun getNoteWithCategories(noteId: Long): NoteWithCategoriesTuple

    @Transaction
    @Query("SELECT * FROM NOTES")
    suspend fun getAllNotesWithCategories(): NoteWithCategoriesTuple

}
