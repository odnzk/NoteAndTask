package com.example.noteapp.ui.fragments.note.detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.noteapp.ui.util.UiState
import com.example.noteapp.ui.util.exceptions.NotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val noteId: Long? by lazy { state.get<Long>("noteId") }
    private var _note: MutableStateFlow<UiState<Note>> = MutableStateFlow(UiState.Loading())
    val note: StateFlow<UiState<Note>> = _note

    private var isNewNote: Boolean = false

    init {
        loadData()
    }

    private fun loadData() {
        _note.value = UiState.Loading()
        viewModelScope.launch {
            noteId?.let {
                val repoNote = noteRepository.getById(it)
                _note.value = repoNote?.let { repoNote -> UiState.Success(repoNote) } ?: run {
                    UiState.Error(NotFoundException())
                }
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
                    noteRepository.add(event.note)
                    isNewNote = false
                } else {
                    noteRepository.update(event.note)
                }
            }
            is NoteDetailedEvent.DeleteNote -> {
                // if UiState.Loading or UiState.Error do nothing
                note.value.data?.let { noteRepository.delete(it) }
            }
            NoteDetailedEvent.TryLoadingNoteAgain -> loadData()
        }
    }

}
