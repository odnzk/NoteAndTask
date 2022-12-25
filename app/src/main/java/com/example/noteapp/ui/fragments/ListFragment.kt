package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.NoteItem
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentListBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.fragments.events.ListFragmentEvent
import com.example.noteapp.ui.recycler.NoteItemAdapter
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
import com.example.noteapp.ui.viewmodel.MainViewModel
import com.example.noteapp.ui.viewmodel.handleState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val listAdapter = NoteItemAdapter()
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        initRecyclerView()
        with(binding) {
            btnAdd.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addTodoBottomSheetDialog)
            }

            btnClearAll.setOnClickListener {
                viewModel.onEvent(ListFragmentEvent.ClearAll)
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            recyclerView.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)

        initAdapter()

        return binding.root
    }

    private fun observeState() {
        lifecycleScope.launchWhenResumed {
            viewModel.noteItemsListState.collectLatest { listState ->
                listState.handleState(
                    onErrorAction = ::onErrorAction,
                    onLoadingAction = { stateLoadingBinding.loadingStarted() },
                    onSuccessAction = ::onSuccessAction
                )
            }
        }
    }

    private fun onErrorAction(error: Throwable) {
        stateLoadingBinding.errorOccurred(error) {
            viewModel.loadData()
        }
    }

    private fun onSuccessAction(data: List<NoteItem>) {
        stateLoadingBinding.loadingFinished()
        listAdapter.submitList(data)
    }

    private fun initAdapter() {
        listAdapter.onNoteClick = ::onNoteClick
        listAdapter.onTodoClick = ::onTodoClick
        listAdapter.onTodoCheckboxClick = { id, isCompleted ->
            viewModel.onEvent(ListFragmentEvent.UpdateTodoCompletedStatus(id, isCompleted))
        }
        listAdapter.submitList(emptyList())
    }

    private fun onNoteClick(noteId: Long) {
        val action = ListFragmentDirections.actionListFragmentToNoteDetailFragment(noteId)
        findNavController().navigate(action)
    }

    private fun onTodoClick(todoId: Long) {
        val action = ListFragmentDirections.actionListFragmentToTodoDetailFragment(todoId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }
}
