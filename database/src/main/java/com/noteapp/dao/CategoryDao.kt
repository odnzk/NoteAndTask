package com.example.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.data.BaseDao
import com.example.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseDao<CategoryEntity> {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(category: CategoryEntity): Long

//    @Delete
//    suspend fun delete(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE category_id = :id")
    suspend fun deleteById(id: Long?)

//    @Update
//    suspend fun update(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE category_id = :id")
    suspend fun getById(id: Long): CategoryEntity?

    @Query("SELECT * FROM categories")
    fun getAll(): Flow<List<CategoryEntity>>

}