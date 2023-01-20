package com.example.domain.repository

import com.example.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun add(todo: Todo): Long

    suspend fun delete(id: Long)

    suspend fun update(todo: Todo)

    fun getAll(): Flow<List<Todo>>

    fun getByTitle(title: String): Flow<List<Todo>>

    suspend fun getById(id: Long): Todo?

    suspend fun deleteAll()

    suspend fun updateCompletedStatus(id: Long, isCompleted: Boolean)

    suspend fun updateCategory(todoId: Long, categoryId: Long)

    suspend fun removeCategory(todoId: Long)
}
