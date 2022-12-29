package com.example.noteapp.di.modules

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.dao.CategoryDao
import com.example.data.dao.NoteDao
import com.example.data.dao.TodoDao
import com.example.data.repository.CategoryRepositoryImpl
import com.example.data.repository.NoteRepositoryImpl
import com.example.data.repository.TodoRepositoryImpl
import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
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
    fun providesNotesDao(database: AppDatabase) = database.noteDao()

    @Singleton
    @Provides
    fun providesTodoDao(database: AppDatabase) = database.todoDao()

    @Singleton
    @Provides
    fun providesCategoryDao(database: AppDatabase) = database.categoryDao()

    @Provides
    @Singleton
    fun providesNoteRepository(noteDao: NoteDao): NoteRepository = NoteRepositoryImpl(noteDao)

    @Provides
    @Singleton
    fun providesTodoRepository(todoDao: TodoDao): TodoRepository = TodoRepositoryImpl(todoDao)

    @Provides
    @Singleton
    fun providesCategoryRepository(categoryDao: CategoryDao): CategoryRepository =
        CategoryRepositoryImpl(categoryDao)

}