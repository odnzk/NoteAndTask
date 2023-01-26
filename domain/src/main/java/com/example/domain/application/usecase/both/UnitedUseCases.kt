package com.example.domain.application.usecase.both

data class UnitedUseCases(
    val deleteAll: DeleteAll,
    val getBothTodosAndNotes: GetBothTodosAndNotes
)
