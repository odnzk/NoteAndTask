package com.example.noteapp.ui.util.exceptions

import com.example.domain.util.Field

open class AppException : RuntimeException()

class LoadingFromDatabaseFailedException : AppException()

class NotFoundException : AppException()

class InvalidNavArgumentsException : AppException()

//class InvalidColorException : AppException()

class InvalidCategoryException(val field: Field) : AppException()
class InvalidNoteException(val field: Field) : AppException()
class InvalidTodoException(val field: Field) : AppException()
