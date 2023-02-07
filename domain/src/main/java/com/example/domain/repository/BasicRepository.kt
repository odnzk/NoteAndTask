package com.example.domain.repository

import kotlinx.coroutines.flow.Flow

interface BasicRepository<T> {
    fun getAll(): Flow<List<T>>
    suspend fun getById(id: Long): T?
    suspend fun add(elem: T): Result<Long>

    // exception if title is not unique
    suspend fun delete(id: Long)
    suspend fun update(elem: T): Result<Long>
    // exception if title is not unique
}
