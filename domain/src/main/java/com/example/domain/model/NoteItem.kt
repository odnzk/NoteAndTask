package com.example.domain.model

import java.util.*

sealed class NoteItem(open val id: Long)

data class Note(
    override val id: Long,
    val title: String,
    val content: String,
    val categories: List<Category>,
    val date: Date?
) : NoteItem(id) {
    companion object {
        fun defaultInstance() = Note(0, "", "", emptyList(), Date())
    }
}

data class Todo(
    override val id: Long,
    var title: String,
    val isCompleted: Boolean = false,
    val categories: List<Category>,
    var deadlineDate: Date?
) : NoteItem(id) {
    companion object {
        fun defaultInstance() = Todo(0, "", false, emptyList(), null)
    }
}
