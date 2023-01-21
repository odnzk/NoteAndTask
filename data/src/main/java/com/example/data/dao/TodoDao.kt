package com.example.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.data.entity.TodoEntity
import com.example.data.tuples.TodoWithCategoryTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = REPLACE)
    suspend fun add(todo: TodoEntity): Long

    @Query("DELETE FROM todos WHERE id = :todoId")
    suspend fun delete(todoId: Long)

    @Update
    suspend fun update(todo: TodoEntity)

    @Query("DELETE FROM todos")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getById(id: Long): TodoWithCategoryTuple?

    @Query("UPDATE todos SET is_completed = :isCompleted WHERE id = :id")
    suspend fun updateIsCompletedById(id: Long, isCompleted: Boolean)

    @Transaction
    @Query("SELECT * FROM todos")
    fun getAll(): Flow<List<TodoWithCategoryTuple>>

    @Transaction
    @Query("SELECT * FROM todos WHERE title LIKE '%' || :title || '%'")
    fun getByTitle(title: String): Flow<List<TodoWithCategoryTuple>>

    @Query("update todos set category_id = null where id = :todoId")
    suspend fun removeTodoCategory(todoId: Long)

    @Query("update todos set category_id =:categoryId where id = :todoId")
    suspend fun updateTodoCategory(todoId: Long, categoryId: Long)

    @Transaction
    @Query("SELECT * FROM todos WHERE category_id = :categoryId AND title LIKE '%' || :title || '%'")
    fun getByCategoryId(categoryId: Long, title: String): Flow<List<TodoWithCategoryTuple>>

}
