package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Todo
import java.util.*

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    @ColumnInfo(name = "deadline_date") val deadlineDate: Date?
) {

    fun toTodo(): Todo = Todo(
        id = id,
        title = title,
        isCompleted = isCompleted,
        category = emptyList(),
        deadlineDate = deadlineDate,
    )

    companion object {
        fun from(todo: Todo): TodoEntity {
            return TodoEntity(
                id = todo.id,
                title = todo.title,
                isCompleted = todo.isCompleted,
                categoryId = null,
                deadlineDate = todo.deadlineDate
            )
        }
    }
}
