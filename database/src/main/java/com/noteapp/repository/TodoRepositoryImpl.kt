package com.noteapp.repository

import com.noteapp.util.mapper.toEntity
import com.noteapp.util.mapper.toTodo
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.domain.util.exceptions.UniqueConstraints
import com.noteapp.dao.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository, BaseRepository() {
    override suspend fun add(elem: Todo): Result<Long> =
        doRequest((UniqueConstraints.TODO_TITLE)) { dao.insert(elem.toEntity()) }

    override suspend fun delete(id: Long) = dao.deleteById(id)

    override suspend fun update(elem: Todo): Result<Int> =
        doRequest(UniqueConstraints.TODO_TITLE) { dao.update(elem.toEntity()) }

    override fun getAll(): Flow<List<Todo>> =
        dao.getAll().map { list -> list.map { todoEntity -> todoEntity.toTodo() } }

    override fun getTodoFlowById(todoId: Long): Flow<Todo?> =
        dao.getFlowById(todoId).map { it?.toTodo() }

    override fun getByTitle(title: String): Flow<List<Todo>> =
        dao.getByTitle(title).map { list -> list.map { todoEntity -> todoEntity.toTodo() } }

    override suspend fun getById(id: Long): Todo? = dao.getById(id)?.toTodo()
    override suspend fun deleteAll() = dao.deleteAll()
    override suspend fun updateCompletedStatus(id: Long, isCompleted: Boolean) =
        dao.updateIsCompletedById(id, isCompleted)

    override suspend fun updateCategory(todoId: Long, categoryId: Long) = dao.updateTodoCategory(todoId, categoryId)
    override suspend fun removeCategory(todoId: Long) = dao.removeTodoCategory(todoId)
    override fun getByCategoriesId(categoryIds: Set<Long>, todoTitle: String): Flow<List<Todo>> =
        dao.getByCategoryId(categoryIds, todoTitle).map { list -> list.map { it.toTodo() } }
}
