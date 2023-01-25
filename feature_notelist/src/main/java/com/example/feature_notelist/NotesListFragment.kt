package com.example.feature_notelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.feature_notelist.databinding.FragmentNotesListBinding
import com.noteapp.core.state.handleState
import com.noteapp.model.Note
import com.noteapp.model.NoteSortOrder
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.*
import com.noteapp.ui.recycler.note.NoteAdapter
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
        init()
    }

    private fun init() {
        with(binding) {
            btnAdd.setOnClickListener {
                // todo
//                findNavController().navigate(R.id.action_notesListFragment_to_noteDetailFragment)
            }
            btnClearAll.setOnClickListener {
                viewModel.onEvent(ListNoteEvent.ClearAll)
            }
            spinnerSort.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    id: Long
                ) {
                    val order: NoteSortOrder = when (position) {
                        1 -> NoteSortOrder.BY_DATE
                        2 -> NoteSortOrder.BY_ALPHABET
                        else -> NoteSortOrder.DEFAULT
                    }
                    viewModel.onEvent(
                        ListNoteEvent.UpdateSortOrder(
                            order
                        )
                    )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) = Unit
            }
        }
    }

    private fun initRecyclerView() =
        binding.recyclerViewNotes.run {
            val itemTouchHelper =
                ItemTouchHelper(com.noteapp.ui.recycler.SwipeCallback(notesAdapter) { removedItem ->
                    viewModel.onEvent(ListNoteEvent.DeleteItem(removedItem as Note))
                    // undo listener
                    val listener =
                        View.OnClickListener { viewModel.onEvent(ListNoteEvent.RestoreItem) }
                    showSnackbar(com.noteapp.ui.R.string.success_delete, listener)
                })
            initStandardVerticalRecyclerView(itemTouchHelper)
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
//                val action =
//                    NotesListFragmentDirections.actionNotesListFragmentToNoteDetailFragment(
//                        noteId
//                    )
//                findNavController().navigate(action) todo
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
