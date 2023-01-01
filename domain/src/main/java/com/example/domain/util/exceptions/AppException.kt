package com.example.noteapp.ui.util.exceptions

import com.example.domain.util.exceptions.Field

open class AppException : RuntimeException()

class LoadingFromDatabaseFailedException : AppException()

class NotFoundException : AppException()

class InvalidNavArgumentsException : AppException()

//class InvalidColorException : AppException()

class InvalidCategoryException(val field: Field) : AppException()
