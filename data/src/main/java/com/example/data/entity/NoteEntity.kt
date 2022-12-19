package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.domain.model.Note

@Entity(
    tableName = "notes", foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    @ColumnInfo(name = "category_ids") val categoryIds: Long? // todo
) {

    fun mapToNote(): Note =
        Note(id = id, title = title, content = content, category = emptyList(), date = null)

    companion object {
        fun toNoteEntity(note: Note): NoteEntity {
            return NoteEntity(
                title = note.title,
                content = note.content,
                categoryIds = null
            )
        }
    }
}

