package com.example.domain.application.usecase.todo

data class TodoUseCases(
    val addTodo: AddTodo,
    val deleteTodo: DeleteTodo,
    val deleteAllTodo: DeleteAllTodos,
    val updateTodo: UpdateTodo,
    val updateIsCompleted: UpdateIsCompleted,
    val updateTodoCategory: UpdateTodoCategory,
    val removeTodoCategory: RemoveTodoCategory,
    val getAllTodos: GetAllTodos,
    val getTodoById: GetTodoById,
    val getTodoFlowById: GetTodoFlowById
)
