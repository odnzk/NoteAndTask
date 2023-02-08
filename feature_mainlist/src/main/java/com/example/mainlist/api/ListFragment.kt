package com.example.mainlist.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.domain.model.NoteItem
import com.example.domain.model.NoteItemFilter
import com.example.feature_mainlist.databinding.FragmentListBinding
import com.example.mainlist.api.noteitem.NoteItemAdapter
import com.example.mainlist.internal.ListFragmentEvent
import com.example.mainlist.internal.ListViewModel
import com.example.mainlist.internal.navigation.toAddCategoryDialog
import com.example.mainlist.internal.navigation.toDetailedNote
import com.example.mainlist.internal.navigation.toDetailedTodo
import com.noteapp.ui.collectAsUiState
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.*
import com.noteapp.ui.observeWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val listAdapter = NoteItemAdapter()
    private val viewModel by viewModels<ListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)

        initAdapter()
        observeState()
        observeCategories()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initClickListeners()
    }

    private fun observeCategories() {
        lifecycleScope.launch {
            viewModel.categories.observeWithLifecycle(viewLifecycleOwner) { categories ->
                categories.toChipGroup(binding.chipgroupCategories,
                    isCheckedStyleEnabled = true,
                    onAddCategoryClick = { findNavController().toAddCategoryDialog() }) { categoryId ->
                    viewModel.onEvent(
                        ListFragmentEvent.UpdateSelectedCategoriesId(categoryId)
                    )
                }
            }
        }
    }

    private fun initClickListeners() {
        with(binding) {
            spinnerFilter.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    (adapter?.getItemAtPosition(position) as? String).also { selectedString ->
                        selectedString?.let {
                            NoteItemFilter.values().iterator().forEach { filter ->
                                if (selectedString.lowercase() == filter.key) {
                                    viewModel.onEvent(
                                        ListFragmentEvent.UpdateFilter(
                                            filter
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                override fun onNothingSelected(adapter: AdapterView<*>?) = Unit
            }

            svFindByTitle.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    p0?.let {
                        viewModel.onEvent(
                            ListFragmentEvent.UpdateSearchQuery(
                                it
                            )
                        )
                    }
                    return false
                }
            })
            btnClearAll.setOnClickListener {
                viewModel.onEvent(ListFragmentEvent.ClearAll)
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            recyclerView.run {
                val itemTouchHelper =
                    ItemTouchHelper(com.noteapp.ui.recycler.SwipeCallback(listAdapter) { removedItem ->
                        viewModel.onEvent(
                            ListFragmentEvent.DeleteItem(
                                removedItem as NoteItem
                            )
                        )
                        // undo listener
                        val listener =
                            View.OnClickListener { viewModel.onEvent(ListFragmentEvent.RestoreItem) }
                        showSnackbar(com.noteapp.ui.R.string.success_delete, listener)
                    })
                initStandardVerticalRecyclerView(itemTouchHelper)
                adapter = listAdapter
            }
        }
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.list.collectAsUiState(
            viewLifecycleOwner,
            onSuccess = ::showList,
            onError = ::showError, onLoading = stateLoadingBinding::loadingStarted
        )
    }

    private fun showError(error: Throwable) =
        stateLoadingBinding.errorOccurred(error) {
            viewModel.onEvent(ListFragmentEvent.Reload)
        }


    private fun showList(data: List<NoteItem>) {
        stateLoadingBinding.loadingFinished()
        listAdapter.submitList(data)
    }

    private fun initAdapter() {
        listAdapter.run {
            onNoteClick = { noteId -> findNavController().toDetailedNote(noteId) }
            onTodoClick = { todoId -> findNavController().toDetailedTodo(todoId) }
            onTodoCheckboxClick = { id, isCompleted ->
                viewModel.onEvent(ListFragmentEvent.UpdateTodoCompletedStatus(id, isCompleted))
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
