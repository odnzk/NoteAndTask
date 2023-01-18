package com.example.noteapp.di.modules

import com.example.domain.application.usecase.both.DeleteAll
import com.example.domain.application.usecase.both.GetBothTodosAndNotes
import com.example.domain.application.usecase.both.UnitedUseCases
import com.example.domain.application.usecase.category.*
import com.example.domain.application.usecase.note.*
import com.example.domain.application.usecase.todo.*
import com.example.domain.model.validation.CategoryValidator
import com.example.domain.model.validation.NoteValidator
import com.example.domain.model.validation.TodoValidator
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
object UseCasesModule {

    @Provides
    @Singleton
    fun providesNoteUseCases(
        noteRepository: NoteRepository, noteValidator: NoteValidator
    ): NoteUseCases = NoteUseCases(
        addNote = AddNote(noteRepository, noteValidator),
        deleteNote = DeleteNote(noteRepository),
        deleteAllNotes = DeleteAllNotes(noteRepository),
        updateNote = UpdateNote(noteRepository, noteValidator),
        addNoteCategory = AddNoteCategory(noteRepository),
        removeNoteCategory = RemoveNoteCategory(noteRepository),
        getAllNotes = GetAllNotes(noteRepository),
        getNoteById = GetNoteById(noteRepository)
    )

    @Provides
    @Singleton
    fun providesTodoUseCases(
        todoRepository: TodoRepository, todoValidator: TodoValidator
    ): TodoUseCases = TodoUseCases(
        addTodo = AddTodo(todoRepository, todoValidator),
        deleteTodo = DeleteTodo(todoRepository),
        deleteAllTodo = DeleteAllTodos(todoRepository),
        updateTodo = UpdateTodo(todoRepository, todoValidator),
        updateIsCompleted = UpdateIsCompleted(todoRepository),
        addTodoCategory = AddTodoCategory(todoRepository),
        removeTodoCategory = RemoveTodoCategory(todoRepository),
        getAllTodos = GetAllTodos(todoRepository),
        getTodoById = GetTodoById(todoRepository)
    )

    @Provides
    @Singleton
    fun providesCategoryUseCases(
        categoryRepository: CategoryRepository, categoryValidator: CategoryValidator
    ): CategoryUseCases = CategoryUseCases(
        addCategory = AddCategory(categoryRepository, categoryValidator),
        deleteCategory = DeleteCategory(categoryRepository),
        updateCategory = UpdateCategory(categoryRepository, categoryValidator),
        getAllCategories = GetAllCategories(categoryRepository)
    )

    @Provides
    @Singleton
    fun providesUnitedUseCases(noteRepository: NoteRepository, todoRepository: TodoRepository) =
        UnitedUseCases(
            deleteAll = DeleteAll(noteRepository = noteRepository, todoRepository = todoRepository),
            getBothTodosAndNotes = GetBothTodosAndNotes(noteRepository, todoRepository)
        )


    @Provides
    @Singleton
    fun providesNoteValidator() = NoteValidator()

    @Provides
    @Singleton
    fun providesTodoValidator() = TodoValidator()

    @Provides
    @Singleton
    fun providesCategoryValidator() = CategoryValidator()
}
