package com.example.domain.repository

import com.example.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun add(todo: Todo): Long

    suspend fun delete(todo: Todo)

    suspend fun update(todo: Todo)

    fun getAll(): Flow<List<Todo>>

    suspend fun getById(id: Long): Todo?

    suspend fun deleteAll()
}
