package com.example.noteapp.ui.fragments.note.detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.model.Note
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases, private val state: SavedStateHandle
) : ViewModel() {

    private val noteId: Long? by lazy { state.get<Long>("noteId") }
    private var _note: MutableStateFlow<UiState<Note>> = MutableStateFlow(UiState.Loading())
    val note: StateFlow<UiState<Note>> = _note.asStateFlow()

    private var isNewNote: Boolean = false

    init {
        loadData()
    }

    private fun loadData() {
        _note.value = UiState.Loading()
        viewModelScope.launch {
            noteId?.let {
                // todo catch exception in getNoteById()
                val repoNote = noteUseCases.getNoteById(it)
                _note.value = UiState.Success(repoNote)
            } ?: run {
                isNewNote = true
                _note.value = UiState.Success(Note.defaultInstance())
            }
        }
    }

    // todo ?? Dispatchers.Default
    fun onEvent(event: NoteDetailedEvent) = viewModelScope.launch(Dispatchers.Default) {
        when (event) {
            is NoteDetailedEvent.UpdateNote -> {
                if (isNewNote) {
                    noteUseCases.addNote(event.note)
                    isNewNote = false
                } else {
                    noteUseCases.updateNote(event.note)
                }
            }
            is NoteDetailedEvent.DeleteNote -> {
                // if UiState.Loading or UiState.Error do nothing
                note.value.data?.let { noteUseCases.deleteNote(it) }
            }
            NoteDetailedEvent.TryLoadingNoteAgain -> loadData()
        }
    }

}
