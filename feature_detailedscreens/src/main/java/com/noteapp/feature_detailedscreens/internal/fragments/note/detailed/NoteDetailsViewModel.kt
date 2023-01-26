package com.noteapp.feature_detailedscreens.internal.fragments.note.detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.model.Note
import com.example.noteapp.ui.util.exceptions.InvalidNavArgumentsException
import com.example.noteapp.ui.util.exceptions.NotFoundException
import com.noteapp.core.constants.Constants
import com.noteapp.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NoteDetailsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases, private val state: SavedStateHandle
) : ViewModel() {

    private val noteId: Long by lazy {
        state.get<Long>("noteId") ?: throw InvalidNavArgumentsException()
    }
    private var _note: MutableStateFlow<UiState<Note>> = MutableStateFlow(UiState.Loading())
    val note = _note.asStateFlow()

    private var isNewNote: Boolean = false

    init {
        loadData()
    }

    private fun loadData() {
        _note.value = UiState.Loading()
        viewModelScope.launch {
            if (noteId == Constants.CREATE_NOTE_LONG) {
                isNewNote = true
                _note.value = UiState.Success(Note.defaultInstance())
            } else {
                noteUseCases.getNoteFlowById(noteId).distinctUntilChanged().collectLatest { note ->
                    _note.update {
                        note?.let { note -> UiState.Success(note) } ?: UiState.Error(
                            NotFoundException()
                        )
                    }
                }
//                noteUseCases.getNoteById(noteId).fold(
//                    onSuccess = { note ->
//                        _note.value = UiState.Success(note)
//                    },
//                    onFailure = { error ->
//                        _note.value = UiState.Error(error)
//                    }
//                )
            }
        }
    }

    // todo Dispatchers
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
