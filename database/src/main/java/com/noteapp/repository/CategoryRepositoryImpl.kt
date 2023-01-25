package com.example.data.repository

import com.example.data.dao.CategoryDao
import com.example.data.mapper.toCategory
import com.example.data.mapper.toEntity
import com.example.domain.repository.CategoryRepository
import com.noteapp.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {

    override suspend fun add(elem: Category): Long {
        return dao.insert(elem.toEntity())
    }

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun update(elem: Category) {
        dao.update(elem.toEntity())
    }

    override suspend fun getById(id: Long): Category? {
        return dao.getById(id)?.toCategory()
    }

    override fun getAll(): Flow<List<Category>> {
        return dao.getAll()
            .map { list -> list.map { it.toCategory() } }
    }
}
