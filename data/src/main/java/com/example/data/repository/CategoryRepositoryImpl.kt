package com.example.data.repository

import com.example.data.dao.CategoryDao
import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {
    override suspend fun add(category: Category): Long {
        return dao.insert(EntityMapper.toCategoryEntity(category))
    }

    override suspend fun delete(category: Category) {
        dao.delete(EntityMapper.toCategoryEntity(category))
    }

    override suspend fun update(category: Category) {
        dao.update(EntityMapper.toCategoryEntity(category))
    }

    override suspend fun getById(id: Long): Category? {
        return dao.getById(id)?.mapToCategory()
    }

    override fun getAll(): Flow<List<Category>> {
        return dao.getAll()
            .map { list -> list.map { categoryEntity -> categoryEntity.mapToCategory() } }
    }
}
