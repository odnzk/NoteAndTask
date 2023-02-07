package com.example.domain.repository

import com.example.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository : BaseRepository<Todo> {

    fun getTodoFlowById(todoId: Long): Flow<Todo?>

    fun getByTitle(title: String): Flow<List<Todo>>

    suspend fun deleteAll()

    suspend fun updateCompletedStatus(id: Long, isCompleted: Boolean)

    suspend fun updateCategory(todoId: Long, categoryId: Long)

    suspend fun removeCategory(todoId: Long)

    fun getByCategoriesId(categoryIds: Set<Long>, todoTitle: String = ""): Flow<List<Todo>>
}
