package com.example.noteapp.ui.fragments.note.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.model.Note
import com.example.noteapp.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private var _notes: MutableStateFlow<UiState<List<Note>>> = MutableStateFlow(UiState.Loading())
    val notes = _notes.asStateFlow()

    private var recentlyRemoved: Note? = null


    init {
        loadData()
    }

    private fun loadData() =
        viewModelScope.launch {
            noteUseCases.getAllNotes().distinctUntilChanged().collectLatest {
                _notes.value = UiState.Success(it)
            }
        }


    fun onEvent(event: ListNoteEvent) =
        viewModelScope.launch {
            when (event) {
                is ListNoteEvent.ClearAll -> noteUseCases.deleteAllNotes()
                is ListNoteEvent.TryAgain -> loadData()
                is ListNoteEvent.DeleteItem -> {
                    noteUseCases.deleteNote(event.note.id)
                    recentlyRemoved = event.note
                }
                ListNoteEvent.RestoreItem -> recentlyRemoved?.let {
                    noteUseCases.addNote(it)
                }
            }
        }

}
