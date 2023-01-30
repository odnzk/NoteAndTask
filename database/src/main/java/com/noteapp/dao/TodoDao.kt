package com.noteapp.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.entity.TodoEntity
import com.example.data.tuples.TodoWithCategoryTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao : BaseDao<TodoEntity> {

    @Query("DELETE FROM todos WHERE id = :todoId")
    suspend fun deleteById(todoId: Long)

    @Transaction
    @Query("SELECT * FROM todos WHERE id = :id")
    fun getFlowById(id: Long): Flow<TodoWithCategoryTuple?>

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
    @Query("SELECT * FROM todos WHERE LOWER(title) LIKE '%' || LOWER(:title) || '%'")
    fun getByTitle(title: String): Flow<List<TodoWithCategoryTuple>>

    @Query("update todos set category_id = null where id = :todoId")
    suspend fun removeTodoCategory(todoId: Long)

    @Query("update todos set category_id =:categoryId where id = :todoId")
    suspend fun updateTodoCategory(todoId: Long, categoryId: Long)

    @Transaction
    @Query("SELECT * FROM todos WHERE category_id in (:categoryIds) AND LOWER(title) LIKE '%' || LOWER(:title) || '%'")
    fun getByCategoryId(categoryIds: Set<Long>, title: String): Flow<List<TodoWithCategoryTuple>>

}
