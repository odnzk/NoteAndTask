package com.example.data.repository

import com.example.data.dao.TodoDao
import com.example.data.mapper.toEntity
import com.example.data.mapper.toTodo
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val dao: TodoDao
) : TodoRepository {
    override suspend fun add(todo: Todo): Long {
        return dao.add(todo.toEntity())
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }

    override suspend fun update(todo: Todo) {
        dao.update(todo.toEntity())
    }

    override fun getAll(): Flow<List<Todo>> =
        dao.getAll().map { list -> list.map { todoEntity -> todoEntity.toTodo() } }


    override fun getByTitle(title: String): Flow<List<Todo>> =
        dao.getByTitle(title).map { list -> list.map { todoEntity -> todoEntity.toTodo() } }

    override suspend fun getById(id: Long): Todo? {
        return dao.getById(id)?.toTodo()
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun updateCompletedStatus(id: Long, isCompleted: Boolean) {
        dao.updateIsCompletedById(id, isCompleted)
    }

    override suspend fun updateCategory(todoId: Long, categoryId: Long) {
        dao.updateTodoCategory(todoId, categoryId)
    }

    override suspend fun removeCategory(todoId: Long) {
        dao.removeTodoCategory(todoId)
    }
}
