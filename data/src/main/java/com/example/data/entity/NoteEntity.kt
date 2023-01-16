package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Note
import java.util.*

@Entity(
    tableName = "notes"
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: Date? = null
) {

    companion object {
        fun from(note: Note): NoteEntity {
            return NoteEntity(
                id = note.id,
                title = note.title,
                content = note.content,
                date = note.date
            )
        }
    }
}

