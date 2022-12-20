package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentDetailedNoteBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.util.exceptions.LoadingFromDatabaseFailedException
import com.example.noteapp.ui.util.exceptions.NotFoundException
import com.example.noteapp.ui.viewmodel.ListState
import com.example.noteapp.ui.viewmodel.MainViewModel
import com.example.noteapp.ui.viewmodel.NoteItemEvent

class NoteDetailFragment : Fragment() {
    private var _binding: FragmentDetailedNoteBinding? = null
    private val binding get() = _binding!!
    private val stateLoadingBinding by lazy {
        StateLoadingBinding.bind(binding.root) // todo memory leak?
    }

    private val viewModel: MainViewModel by viewModels()

    private val selectedNoteId: NoteDetailFragmentArgs by navArgs()
    private var selectedNote: Note =
        Note(id = 1, title = "", content = "", category = emptyList(), date = null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            observeState()

            btnDelete.setOnClickListener {
                viewModel.onEvent(NoteItemEvent.DeleteItem(selectedNote))
            }
            etContent.doAfterTextChanged {
                selectedNote = selectedNote.copy(content = etContent.text.toString())
                viewModel.onEvent(NoteItemEvent.UpdateNoteItem(selectedNote))
            }
            etTitle.doAfterTextChanged {
                selectedNote = selectedNote.copy(title = etTitle.text.toString())
                viewModel.onEvent(NoteItemEvent.UpdateNoteItem(selectedNote))
            }
            categoryLayout.category.setOnClickListener {
                // todo change category
                // 1) show dialog and get curr id to him
                // 2) viewModel.onEvent(NoteItemEvent.UpdateNoteItem(displayedNote)) from dialog
            }
        }
    }

    private fun observeState() {
        when (val state = viewModel.noteItemsListState) {
            is ListState.Loading -> showLoading()
            is ListState.Success<*> -> {
                finishLoading()
                displaySelectedNote(state.data as List<NoteItem>) //todo fix
            }
            is ListState.Error -> showError(state.error)
        }
    }

    private fun showError(error: Throwable) {
        with(stateLoadingBinding) {
            val resId = when (error) {
                is NotFoundException -> R.string.error_not_found
                is LoadingFromDatabaseFailedException -> R.string.error_failed_loading_from_database
                else -> R.string.error_unknown
            }
            pbLoading.hide()
            btnTryLoadingAgain.isVisible = true
            tvErrorLoading.text = getString(resId)
            btnTryLoadingAgain.setOnClickListener { viewModel.loadData() }
        }
    }

    private fun finishLoading() {
        with(stateLoadingBinding) {
            stateLoadingBinding.pbLoading.hide() // ??
            root.isVisible = false
        }
    }

    private fun displaySelectedNote(data: List<NoteItem>) {
        selectedNote = data.filter { it.id == selectedNoteId }
    }

    private fun showLoading() {
        with(stateLoadingBinding) {
            pbLoading.show()
            tvErrorLoading.isVisible = false
            btnTryLoadingAgain.isVisible = false
            root.isVisible = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
