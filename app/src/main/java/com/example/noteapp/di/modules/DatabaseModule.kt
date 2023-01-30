package com.example.noteapp.di.modules

import android.app.Application
import androidx.room.Room
import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import com.noteapp.AppDatabase
import com.noteapp.dao.CategoryDao
import com.noteapp.dao.NoteDao
import com.noteapp.dao.TodoDao
import com.noteapp.repository.CategoryRepositoryImpl
import com.noteapp.repository.NoteRepositoryImpl
import com.noteapp.repository.TodoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(app: Application) =
        Room.databaseBuilder(app, AppDatabase::class.java, "main_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesNotesDao(database: AppDatabase): NoteDao = database.noteDao()

    @Singleton
    @Provides
    fun providesTodoDao(database: AppDatabase): TodoDao = database.todoDao()

    @Singleton
    @Provides
    fun providesCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

}
