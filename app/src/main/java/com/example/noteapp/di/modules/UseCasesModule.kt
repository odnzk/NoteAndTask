package com.example.noteapp.di.modules

import com.example.domain.application.usecase.both.DeleteAll
import com.example.domain.application.usecase.both.GetBothTodosAndNotes
import com.example.domain.application.usecase.both.UnitedUseCases
import com.example.domain.application.usecase.category.*
import com.example.domain.application.usecase.note.*
import com.example.domain.application.usecase.todo.*
import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.NoteRepository
import com.example.domain.repository.TodoRepository
import com.example.domain.validation.CategoryValidator
import com.example.domain.validation.NoteValidator
import com.example.domain.validation.TodoValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun providesCoroutineDispatcher() = Dispatchers.Default

    @Provides
    @Singleton
    fun providesNoteUseCases(
        noteRepository: NoteRepository,
        noteValidator: NoteValidator,
        dispatcher: CoroutineDispatcher
    ): NoteUseCases = NoteUseCases(
        addNote = AddNote(noteRepository, noteValidator, dispatcher),
        deleteNote = DeleteNote(noteRepository, dispatcher),
        deleteAllNotes = DeleteAllNotes(noteRepository, dispatcher),
        updateNote = UpdateNote(noteRepository, noteValidator, dispatcher),
        addNoteCategory = AddNoteCategory(noteRepository, dispatcher),
        removeNoteCategory = RemoveNoteCategory(noteRepository, dispatcher),
        getAllNotes = GetAllNotes(noteRepository, dispatcher),
        getNoteById = GetNoteById(noteRepository, dispatcher),
        getNoteFlowById = GetNoteFlowById(noteRepository, dispatcher)
    )

    @Provides
    @Singleton
    fun providesTodoUseCases(
        todoRepository: TodoRepository,
        todoValidator: TodoValidator,
        dispatcher: CoroutineDispatcher
    ): TodoUseCases = TodoUseCases(
        addTodo = AddTodo(todoRepository, todoValidator, dispatcher),
        deleteTodo = DeleteTodo(todoRepository, dispatcher),
        deleteAllTodo = DeleteAllTodos(todoRepository, dispatcher),
        updateTodo = UpdateTodo(todoRepository, todoValidator, dispatcher),
        updateIsCompleted = UpdateIsCompleted(todoRepository, dispatcher),
        updateTodoCategory = UpdateTodoCategory(todoRepository, dispatcher),
        removeTodoCategory = RemoveTodoCategory(todoRepository, dispatcher),
        getAllTodos = GetAllTodos(todoRepository, dispatcher),
        getTodoById = GetTodoById(todoRepository, dispatcher),
        getTodoFlowById = GetTodoFlowById(todoRepository, dispatcher)
    )

    @Provides
    @Singleton
    fun providesCategoryUseCases(
        categoryRepository: CategoryRepository,
        categoryValidator: CategoryValidator,
        dispatcher: CoroutineDispatcher
    ): CategoryUseCases = CategoryUseCases(
        addCategory = AddCategory(categoryRepository, categoryValidator, dispatcher),
        deleteCategory = DeleteCategory(categoryRepository, dispatcher),
        updateCategory = UpdateCategory(categoryRepository, categoryValidator, dispatcher),
        getAllCategories = GetAllCategories(categoryRepository, dispatcher)
    )

    @Provides
    @Singleton
    fun providesUnitedUseCases(
        noteRepository: NoteRepository,
        todoRepository: TodoRepository,
        dispatcher: CoroutineDispatcher
    ) =
        UnitedUseCases(
            deleteAll = DeleteAll(
                noteRepository = noteRepository,
                todoRepository = todoRepository,
                dispatcher
            ),
            getBothTodosAndNotes = GetBothTodosAndNotes(noteRepository, todoRepository, dispatcher)
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
