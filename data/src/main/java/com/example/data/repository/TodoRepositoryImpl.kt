package com.example.data.repository

import com.example.data.dao.TodoDao
import com.example.data.entity.TodoEntity
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(private val dao: TodoDao) : TodoRepository {
    override suspend fun add(todo: Todo): Long {
        return dao.add(TodoEntity.from(todo))
    }

    override suspend fun delete(todo: Todo) {
        dao.delete(TodoEntity.from(todo))
    }

    override suspend fun update(todo: Todo) {
        dao.update(TodoEntity.from(todo))
    }

    override fun getAll(): Flow<List<Todo>> {
        return dao.getAll().map { list -> list.map { todoEntity -> todoEntity.toTodo() } }
    }

    override suspend fun getById(id: Long): Todo? {
        return dao.getById(id)?.toTodo()
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
