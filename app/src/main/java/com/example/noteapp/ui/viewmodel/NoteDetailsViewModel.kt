package com.example.noteapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.noteapp.ui.util.exceptions.LostNavArgumentsException
import com.example.noteapp.ui.util.exceptions.NotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
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
            } ?: run { _note.value = UiState.Error(LostNavArgumentsException()) }
        }
    }

}
