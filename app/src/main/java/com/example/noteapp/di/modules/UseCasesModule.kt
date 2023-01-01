package com.example.noteapp.di.modules

import com.example.domain.application.usecase.category.*
import com.example.domain.application.usecase.note.*
import com.example.domain.application.usecase.todo.*
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
class UseCasesModule {

    @Provides
    @Singleton
    fun providesNoteUseCases(noteRepository: NoteRepository): NoteUseCases = NoteUseCases(
        addNote = AddNote(noteRepository),
        deleteNote = DeleteNote(noteRepository),
        deleteAllNotes = DeleteAllNotes(noteRepository),
        updateNote = UpdateNote(noteRepository),
        addNoteCategory = AddNoteCategory(noteRepository),
        removeNoteCategory = RemoveNoteCategory(noteRepository),
        getAllNotes = GetAllNotes(noteRepository),
        getNoteById = GetNoteById(noteRepository)
    )

    @Provides
    @Singleton
    fun providesTodoUseCases(todoRepository: TodoRepository): TodoUseCases =
        TodoUseCases(
            addTodo = AddTodo(todoRepository),
            deleteTodo = DeleteTodo(todoRepository),
            deleteAllTodo = DeleteAllTodos(todoRepository),
            updateTodo = UpdateTodo(todoRepository),
            updateIsCompleted = UpdateIsCompleted(todoRepository),
            addTodoCategory = AddTodoCategory(todoRepository),
            removeTodoCategory = RemoveTodoCategory(todoRepository),
            getAllTodos = GetAllTodos(todoRepository),
            getTodoById = GetTodoById(todoRepository)
        )

    @Provides
    @Singleton
    fun providesCategoryUseCases(categoryRepository: CategoryRepository): CategoryUseCases =
        CategoryUseCases(
            addCategory = AddCategory(categoryRepository),
            deleteCategory = DeleteCategory(categoryRepository),
            updateCategory = UpdateCategory(categoryRepository),
            getAllCategories = GetAllCategories(categoryRepository)
        )
}
