package com.example.domain.model

import java.util.*
import kotlin.time.Duration

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
    val category: Category?,
    var deadlineDate: Date? = null,
    val reminderCalendar: Calendar? = null, // todo
    val periodicity: Duration? = null // todo
) : NoteItem(id) {
    companion object {
        fun defaultInstance() = Todo(0, "", false, null, null)
    }
}

