package com.example.feature_notelist.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.note.NoteUseCases
import com.example.domain.model.Note
import com.example.domain.model.NoteSortOrder
import com.noteapp.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {
    private var _notes: MutableStateFlow<UiState<List<Note>>> = MutableStateFlow(UiState.Loading())
    val notes = _notes.asStateFlow()
    private var recentlyRemoved: Note? = null
    private var noteSortOrder: NoteSortOrder = NoteSortOrder.DEFAULT
    private var jobObservingNoteList: Job? = null

    init {
        loadData()
    }

    private fun loadData(noteSort: NoteSortOrder = NoteSortOrder.DEFAULT) {
        jobObservingNoteList?.cancel()
        jobObservingNoteList = viewModelScope.launch {
            noteUseCases.getAllNotes(noteSort).distinctUntilChanged().collectLatest { list ->
                _notes.update { UiState.Success(list) }
            }
        }
    }


    fun onEvent(event: ListNoteEvent) = viewModelScope.launch {
        when (event) {
            is ListNoteEvent.DeleteAllNotes -> noteUseCases.deleteAllNotes()
            is ListNoteEvent.Reload -> loadData()
            is ListNoteEvent.DeleteNote -> {
                noteUseCases.deleteNote(event.note.id)
                recentlyRemoved = event.note
            }
            ListNoteEvent.RestoreNote -> recentlyRemoved?.let { noteUseCases.addNote(it) }
            is ListNoteEvent.UpdateSortOrder -> {
                if (noteSortOrder != event.noteSortOrder) {
                        noteSortOrder = event.noteSortOrder
                        loadData(noteSortOrder)
                    }
                }
            }
        }
}
