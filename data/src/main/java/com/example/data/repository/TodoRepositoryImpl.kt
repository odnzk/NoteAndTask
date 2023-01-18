package com.example.data.repository

import com.example.data.dao.TodoDao
import com.example.data.mapper.TodoMapper
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val dao: TodoDao,
    private val mapper: TodoMapper
) : TodoRepository {
    override suspend fun add(todo: Todo): Long {
        return dao.add(mapper.from(todo))
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }

    override suspend fun update(todo: Todo) {
        dao.update(mapper.from(todo))
    }

    override fun getAll(): Flow<List<Todo>> =
        dao.getAll().map { list -> list.map { todoEntity -> mapper.toTodo(todoEntity) } }


    override fun getByTitle(title: String): Flow<List<Todo>> =
        dao.getByTitle(title).map { list -> list.map { todoEntity -> mapper.toTodo(todoEntity) } }

    override suspend fun getById(id: Long): Todo? {
        return dao.getById(id)?.let { mapper.toTodo(it) }
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun updateCompletedStatus(id: Long, isCompleted: Boolean) {
        dao.updateIsCompletedById(id, isCompleted)
    }

    override suspend fun addCategory(todoId: Long, categoryId: Long) {
        dao.insertTodoCategory(todoId, categoryId)
    }

    override suspend fun removeCategory(todoId: Long, categoryId: Long) {
        dao.removeTodoCategory(todoId, categoryId)
    }
}
