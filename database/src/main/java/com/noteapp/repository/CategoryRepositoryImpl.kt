package com.noteapp.repository

import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import com.example.domain.util.exceptions.UniqueConstraints
import com.noteapp.dao.CategoryDao
import com.noteapp.util.mapper.toCategory
import com.noteapp.util.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository, BaseRepository() {

    override suspend fun add(elem: Category): Result<Long> =
        doRequest(UniqueConstraints.CATEGORY_TITLE) { dao.insert(elem.toEntity()) }

    override suspend fun delete(id: Long) = dao.deleteById(id)

    override suspend fun update(elem: Category): Result<Int> =
        doRequest(UniqueConstraints.CATEGORY_TITLE) { dao.update(elem.toEntity()) }

    override suspend fun getById(id: Long): Category? = dao.getById(id)?.toCategory()

    override fun getAll(): Flow<List<Category>> =
        dao.getAll().map { list -> list.map { it.toCategory() } }

}
