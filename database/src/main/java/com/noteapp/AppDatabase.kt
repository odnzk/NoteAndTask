package com.noteapp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.CategoryDao
import com.example.data.dao.NoteDao
import com.example.data.dao.TodoDao
import com.example.data.entity.*
import com.example.data.entity.jtable.NoteCategoryTable
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