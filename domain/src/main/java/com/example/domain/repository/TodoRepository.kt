package com.example.domain.repository

import com.noteapp.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository : BasicRepository<Todo> {

    fun getByTitle(title: String): Flow<List<Todo>>

    suspend fun deleteAll()

    suspend fun updateCompletedStatus(id: Long, isCompleted: Boolean)

    suspend fun updateCategory(todoId: Long, categoryId: Long)

    suspend fun removeCategory(todoId: Long)

    fun getByCategoryId(categoryId: Long, todoTitle: String): Flow<List<Todo>>
}
