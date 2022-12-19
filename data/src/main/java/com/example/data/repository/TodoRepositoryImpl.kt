package com.example.data.repository

import com.example.data.dao.TodoDao
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(private val dao: TodoDao) : TodoRepository {
    override suspend fun add(todo: Todo): Long {
        return dao.add(EntityMapper.toTodoEntity(todo))
    }

    override suspend fun delete(todo: Todo) {
        dao.delete(EntityMapper.toTodoEntity(todo))
    }

    override suspend fun update(todo: Todo) {
        dao.update(EntityMapper.toTodoEntity(todo))
    }

    override fun getAll(): Flow<List<Todo>> {
        return dao.getAll().map { list -> list.map { todoEntity -> todoEntity.maptoTodo() } }
    }

    override suspend fun getById(id: Long): Todo? {
        return dao.getById(id)?.maptoTodo()
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
