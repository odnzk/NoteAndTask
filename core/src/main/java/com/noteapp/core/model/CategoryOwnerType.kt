package com.noteapp.core.model

// helper model for correct navigation to the ChooseCategoryDialog
enum class CategoryOwnerType(val key: String) {
    NOTE_TYPE(key = "noteId"), TODO_TYPE(key = "todoId")
}
