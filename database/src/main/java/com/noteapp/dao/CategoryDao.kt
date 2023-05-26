package com.noteapp.dao

import androidx.room.Dao
import androidx.room.Query
import com.noteapp.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseDao<CategoryEntity> {
    @Query("DELETE FROM categories WHERE category_id = :id")
    suspend fun deleteById(id: Long?)
    @Query("SELECT * FROM categories WHERE category_id = :id")
    suspend fun getById(id: Long): CategoryEntity?
    @Query("SELECT * FROM categories")
    fun getAll(): Flow<List<CategoryEntity>>
}
