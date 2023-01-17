package com.example.noteapp.ui.fragments.note.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.Note
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentNotesListBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.recycler.note.NoteAdapter
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.ext.initStandardVerticalRecyclerView
import com.example.noteapp.ui.util.handleState
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NotesListFragment : Fragment() {
    private var _binding: FragmentNotesListBinding? = null
    private val binding: FragmentNotesListBinding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val viewModel: ListNoteViewModel by viewModels()
    private val notesAdapter: NoteAdapter = NoteAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNotes()
        initRecyclerView()
        with(binding) {
            btnAdd.setOnClickListener {
                findNavController().navigate(R.id.action_notesListFragment_to_noteDetailFragment)
            }
            btnClearAll.setOnClickListener {
                viewModel.onEvent(ListNoteEvent.ClearAll)
            }
        }
    }

    private fun initRecyclerView() =
        binding.recyclerViewNotes.run {
            initStandardVerticalRecyclerView()
            adapter = notesAdapter
        }

    private fun observeNotes() =
        lifecycleScope.launchWhenStarted {
            viewModel.notes.collectLatest { state ->
                state.handleState(
                    onLoadingAction = stateLoadingBinding::loadingStarted,
                    onSuccessAction = ::showNotes,
                    onErrorAction = ::showError,
                )
            }
        }

    private fun showError(throwable: Throwable) =
        stateLoadingBinding.errorOccurred(throwable) {
            viewModel.onEvent(ListNoteEvent.TryAgain)
        }

    private fun showNotes(notes: List<Note>) {
        stateLoadingBinding.loadingFinished()
        notesAdapter.submitList(notes)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)
        initAdapter()
        return binding.root
    }

    private fun initAdapter() {
        notesAdapter.apply {
            onNoteClick = { noteId ->
                val action =
                    NotesListFragmentDirections.actionNotesListFragmentToNoteDetailFragment(
                        noteId
                    )
                findNavController().navigate(action)
            }
            submitList(emptyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }
}