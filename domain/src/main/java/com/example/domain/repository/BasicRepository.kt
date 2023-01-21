package com.example.domain.repository

import kotlinx.coroutines.flow.Flow

// get all
// getById
// insert
// delete
// update
interface BasicRepository<T> {
    fun getAll(): Flow<List<T>>
    suspend fun getById(id: Long): T?
    suspend fun add(elem: T): Long
    suspend fun delete(id: Long)
    suspend fun update(elem: T)
}
