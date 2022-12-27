package com.example.domain.repository

import com.example.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun add(category: Category): Long

    suspend fun delete(category: Category)

    suspend fun deleteById(id: Long?)

    suspend fun update(category: Category)

    suspend fun getById(id: Long): Category?

    fun getAll(): Flow<List<Category>>
}
