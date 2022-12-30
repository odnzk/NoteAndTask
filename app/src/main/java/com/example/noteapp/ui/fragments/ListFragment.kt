package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.NoteItem
import com.example.noteapp.databinding.FragmentListBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.fragments.events.ListFragmentEvent
import com.example.noteapp.ui.recycler.noteitem.NoteItemAdapter
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.ext.categoriesToFlowCategories
import com.example.noteapp.ui.util.ext.initStandardVerticalRecyclerView
import com.example.noteapp.ui.util.handleState
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
import com.example.noteapp.ui.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val listAdapter = NoteItemAdapter()
    private val viewModel by viewModels<ListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        observeCategories()
        initRecyclerView()

        with(binding) {
            btnClearAll.setOnClickListener {
                viewModel.onEvent(ListFragmentEvent.ClearAll)
            }
            chipAddCategory.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToAddCategoryDialog()
                findNavController().navigate(action)
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            recyclerView.run {
                initStandardVerticalRecyclerView()
                adapter = listAdapter
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

    private fun observeState() =
        lifecycleScope.launchWhenStarted {
            viewModel.noteItemsListState.collect { listState ->
                listState.handleState(
                    onErrorAction = ::onErrorAction,
                    onLoadingAction = { stateLoadingBinding.loadingStarted() },
                    onSuccessAction = ::onSuccessAction
                )
            }
        }

    private fun observeCategories() = lifecycleScope.launchWhenStarted {
        viewModel.categoryList.collect { categories ->
            categories.categoriesToFlowCategories(binding.root, binding.flowCategories) {
                // todo on category click
            }
        }
    }

    private fun onErrorAction(error: Throwable) =
        stateLoadingBinding.errorOccurred(error) {
            viewModel.loadData()
        }


    private fun onSuccessAction(data: List<NoteItem>) {
        stateLoadingBinding.loadingFinished()
        listAdapter.submitList(data)
    }

    private fun initAdapter() {
        listAdapter.onNoteClick = { noteId ->
            val action = ListFragmentDirections.actionListFragmentToNoteDetailFragment(noteId)
            findNavController().navigate(action)
        }
        listAdapter.onTodoClick = { todoId ->
            val action = ListFragmentDirections.actionListFragmentToTodoDetailFragment(todoId)
            findNavController().navigate(action)
        }
        listAdapter.onTodoCheckboxClick = { id, isCompleted ->
            viewModel.onEvent(ListFragmentEvent.UpdateTodoCompletedStatus(id, isCompleted))
        }
        listAdapter.submitList(emptyList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }
}
