package com.example.noteapp.ui.util.exceptions

open class AppException : RuntimeException()

class LoadingFromDatabaseFailedException : AppException()

class NotFoundException : AppException()

class LostNavArgumentsException : AppException()
