package com.example.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.data.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = REPLACE)
    suspend fun add(todo: TodoEntity): Long

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Update
    suspend fun update(todo: TodoEntity)

    @Query("DELETE FROM todos")
    suspend fun deleteAll()

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getById(id: Long): TodoEntity?

    @Query("UPDATE todos SET is_completed = :isCompleted WHERE id = :id")
    suspend fun updateIsCompletedById(id: Long, isCompleted: Boolean)

    @Query("SELECT * FROM todos")
    fun getAll(): Flow<List<TodoEntity>>

    @Query("delete from todo_categories_table where todo_id = :todoId AND category_id =:categoryId")
    suspend fun removeTodoCategory(todoId: Long, categoryId: Long)

    @Query("insert into todo_categories_table values (:todoId, :categoryId)")
    suspend fun insertTodoCategory(todoId: Long, categoryId: Long)
}
