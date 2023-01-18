package com.example.noteapp.di.modules

import com.example.data.mapper.CategoryMapper
import com.example.data.mapper.NoteMapper
import com.example.data.mapper.TodoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Singleton
    @Provides
    fun providesNoteMapper(categoryMapper: CategoryMapper): NoteMapper = NoteMapper(categoryMapper)

    @Singleton
    @Provides
    fun providesTodoMapper(): TodoMapper = TodoMapper()

    @Singleton
    @Provides
    fun providesCategoryMapper(): CategoryMapper = CategoryMapper()
}
