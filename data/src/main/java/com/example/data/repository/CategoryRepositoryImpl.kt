package com.example.data.repository

import com.example.data.dao.CategoryDao
import com.example.data.mapper.CategoryMapper
import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val dao: CategoryDao,
    private val mapper: CategoryMapper
) : CategoryRepository {
    override suspend fun add(category: Category): Long {
        return dao.insert(mapper.from(category))
    }

    override suspend fun delete(category: Category) {
        dao.delete(mapper.from(category))
    }

    override suspend fun deleteById(id: Long?) {
        dao.deleteById(id)
    }

    override suspend fun update(category: Category) {
        dao.update(mapper.from(category))
    }

    override suspend fun getById(id: Long): Category? {
        return dao.getById(id)?.let { mapper.toCategory(it) }
    }

    override fun getAll(): Flow<List<Category>> {
        return dao.getAll()
            .map { list -> list.map { categoryEntity -> mapper.toCategory(categoryEntity) } }
    }
}
