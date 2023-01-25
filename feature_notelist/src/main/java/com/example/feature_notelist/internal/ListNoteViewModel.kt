package com.example.feature_notelist.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.application.usecase.note.NoteUseCases
import com.noteapp.core.state.UiState
import com.noteapp.model.Note
import com.noteapp.model.NoteSortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
            noteUseCases.getAllNotes(noteSort).distinctUntilChanged().collectLatest {
                _notes.value = UiState.Success(it)
            }
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
                is ListNoteEvent.UpdateSortOrder -> {
                    if (noteSortOrder != event.noteSortOrder) {
                        noteSortOrder = event.noteSortOrder
                        loadData(noteSortOrder)
                    }
                }
            }
        }
}
