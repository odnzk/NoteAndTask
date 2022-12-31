package com.example.noteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.noteapp.ui.fragments.events.ListNoteEvent
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var _notes: MutableStateFlow<UiState<List<Note>>> = MutableStateFlow(UiState.Loading())
    val notes: StateFlow<UiState<List<Note>>> = _notes

    init {
        loadData()
    }

    private fun loadData() =
        viewModelScope.launch {
            noteRepository.getAll().collect {
                _notes.value = UiState.Success(it)
            }
        }


    fun onEvent(event: ListNoteEvent) =
        viewModelScope.launch {
            when (event) {
                is ListNoteEvent.ClearAll -> noteRepository.deleteAll()
                is ListNoteEvent.TryAgain -> loadData()
            }
        }

}
