package com.example.data.repository

import com.example.data.dao.CategoryDao
import com.example.data.mapper.toCategory
import com.example.data.mapper.toEntity
import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {
    override suspend fun add(category: Category): Long {
        return dao.insert(category.toEntity())
    }

    override suspend fun delete(category: Category) {
        dao.delete(category.toEntity())
    }

    override suspend fun deleteById(id: Long?) {
        dao.deleteById(id)
    }

    override suspend fun update(category: Category) {
        dao.update(category.toEntity())
    }

    override suspend fun getById(id: Long): Category? {
        return dao.getById(id)?.toCategory()
    }

    override fun getAll(): Flow<List<Category>> {
        return dao.getAll()
            .map { list -> list.map { it.toCategory() } }
    }
}
