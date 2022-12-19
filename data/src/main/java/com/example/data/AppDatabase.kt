package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dao.CategoryDao
import com.example.data.dao.NoteDao
import com.example.data.dao.TodoDao
import com.example.data.entity.CategoryEntity
import com.example.data.entity.NoteEntity
import com.example.data.entity.TodoEntity

@Database(entities = [NoteEntity::class, TodoEntity::class, CategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun todoDao(): TodoDao
    abstract fun categoryDao(): CategoryDao
}
