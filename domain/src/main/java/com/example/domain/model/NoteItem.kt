package com.example.domain.model

import java.util.*

sealed class NoteItem(open val id: Long)

data class Note(
    override val id: Long,
    val title: String,
    val content: String,
    val category: List<Category>,
    val date: Date?
) : NoteItem(id)

data class Todo(
    override val id: Long,
    var title: String,
    val isCompleted: Boolean = false,
    val category: List<Category>,
    val deadlineDate: Date?
) : NoteItem(id) {
    companion object {
        fun defaultTodo() = Todo(0, "", false, emptyList(), null)
    }
}
