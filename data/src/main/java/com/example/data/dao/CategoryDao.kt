package com.example.data.dao

import androidx.room.*
import com.example.data.entity.CategoryEntity
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


    // notes + categories
//    @Query(
//        "SELECT * from categories " +
//                " inner join note_categories_table" +
//                " where note_categories_table.category_id = categories.id" +
//                " and note_categories_table.note_id = :noteId"
//    )
//    fun getLinkedCategoriesByNoteId(noteId: Long): Flow<List<CategoryEntity>>


}
