package com.noteapp.repository

import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import com.noteapp.dao.CategoryDao
import com.noteapp.util.mapper.toCategory
import com.noteapp.util.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository, BaseRepository() {

    override suspend fun add(elem: Category): Result<Long> {
        return doRequest { dao.insert(elem.toEntity()) }
    }

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun update(elem: Category): Result<Int> {
        return doRequest { dao.update(elem.toEntity()) }
    }

    override suspend fun getById(id: Long): Category? {
        return dao.getById(id)?.toCategory()
    }

    override fun getAll(): Flow<List<Category>> {
        return dao.getAll()
            .map { list -> list.map { it.toCategory() } }
    }
}
