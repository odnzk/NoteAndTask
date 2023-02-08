package com.example.domain.util.exceptions

open class AppException : RuntimeException()

class LoadingFromDatabaseFailedException : AppException()

class NotFoundException : AppException()

class InvalidNavArgumentsException : AppException()

//class InvalidColorException : AppException()

class InvalidCategoryException(val field: Field) : AppException()
class InvalidNoteException(val field: Field) : AppException()
class InvalidTodoException(val field: Field) : AppException()

class NotUniqueFieldException(val constraint: UniqueConstraints) : AppException()

enum class UniqueConstraints {
    CATEGORY_TITLE, NOTE_TITLE, TODO_TITLE
}

enum class Field {
    TITLE,
    CONTENT
}
