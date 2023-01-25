package com.noteapp.feature_detailedscreens.fragments.note.detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.note.NoteUseCases
import com.noteapp.core.state.UiState
import com.noteapp.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases, private val state: SavedStateHandle
) : ViewModel() {

    val noteId: Long? by lazy { state.get<Long>("noteId") }
    private var _note: MutableStateFlow<UiState<Note>> = MutableStateFlow(UiState.Loading())
    val note = _note.asStateFlow()

    private var isNewNote: Boolean = false

    init {
        loadData()
    }

    private fun loadData() {
        _note.value = UiState.Loading()
        viewModelScope.launch {
            noteId?.let {
                noteUseCases.getNoteById(it).fold(
                    onSuccess = { note ->
                        _note.value = UiState.Success(note)
                    },
                    onFailure = { error ->
                        _note.value = UiState.Error(error)
                    }
                )
            } ?: run {
                isNewNote = true
                _note.value = UiState.Success(Note.defaultInstance())
            }
        }
    }

    fun onEvent(event: NoteDetailedEvent) = viewModelScope.launch(Dispatchers.Default) {
        when (event) {
            is NoteDetailedEvent.UpdateNote -> {
                if (isNewNote) {
                    noteUseCases.addNote(event.note).also { result ->
                        result.exceptionOrNull()?.let { _note.value = UiState.Error(it) }
                    }
                    isNewNote = false
                } else {
                    noteUseCases.updateNote(event.note).also { result ->
                        result.exceptionOrNull()?.let { _note.value = UiState.Error(it) }
                    }
                }
            }
            is NoteDetailedEvent.DeleteNote -> {
                // if UiState.Loading or UiState.Error do nothing
                note.value.data?.let { noteUseCases.deleteNote(it.id) }
            }
            NoteDetailedEvent.TryLoadingNoteAgain -> loadData()
        }
    }

}
