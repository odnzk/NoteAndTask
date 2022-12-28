package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.CategoryDao
import com.example.data.dao.NoteDao
import com.example.data.dao.TodoDao
import com.example.data.entity.CategoryEntity
import com.example.data.entity.NoteCategoryTable
import com.example.data.entity.NoteEntity
import com.example.data.entity.TodoEntity
import com.example.data.util.RoomTypeConverter

@Database(
    entities = [NoteEntity::class, TodoEntity::class, CategoryEntity::class, NoteCategoryTable::class],
    version = 1
)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun todoDao(): TodoDao
    abstract fun categoryDao(): CategoryDao
}
