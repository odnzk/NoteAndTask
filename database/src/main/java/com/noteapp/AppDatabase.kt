package com.noteapp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.noteapp.entity.jtable.NoteCategoryTable
import com.noteapp.dao.CategoryDao
import com.noteapp.dao.NoteDao
import com.noteapp.dao.TodoDao
import com.noteapp.entity.CategoryEntity
import com.noteapp.entity.NoteEntity
import com.noteapp.entity.TodoEntity
import com.noteapp.util.RoomTypeConverter

@Database(
    entities = [
        NoteEntity::class,
        TodoEntity::class,
        CategoryEntity::class,
        NoteCategoryTable::class
    ],
    version = 1
)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun todoDao(): TodoDao
    abstract fun categoryDao(): CategoryDao
}
