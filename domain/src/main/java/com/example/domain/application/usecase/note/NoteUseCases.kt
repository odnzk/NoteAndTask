package com.example.domain.application.usecase.note

data class NoteUseCases(
    val addNote: AddNote,
    val deleteNote: DeleteNote,
    val deleteAllNotes: DeleteAllNotes,
    val addNoteCategory: AddNoteCategory,
    val removeNoteCategory: RemoveNoteCategory,
    val updateNote: UpdateNote,
    val getAllNotes: GetAllNotes,
    val getNoteById: GetNoteById,
    val getNoteFlowById: GetNoteFlowById
)
