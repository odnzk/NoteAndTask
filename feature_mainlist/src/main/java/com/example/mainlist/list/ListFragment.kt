package com.example.mainlist.list

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
import com.example.feature_mainlist.databinding.FragmentListBinding
import com.example.mainlist.navigation.toAddCategoryDialog
import com.example.mainlist.navigation.toDetailedNote
import com.example.mainlist.navigation.toDetailedTodo
import com.example.mainlist.noteitem.NoteItemAdapter
import com.google.android.material.chip.Chip
import com.noteapp.core.state.handleState
import com.noteapp.model.Filter
import com.noteapp.model.NoteItem
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


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
        initRecyclerView()
        initAll()

    }

    private fun initAll() {
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
                            Filter.values().iterator().forEach { filter ->
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
            viewModel.listState.collectLatest { listState ->
                listState.handleState(
                    onErrorAction = ::onErrorAction,
                    onLoadingAction = { stateLoadingBinding.loadingStarted() },
                    onSuccessAction = ::onSuccessAction
                )
            }
        }

    private fun onErrorAction(error: Throwable) =
        stateLoadingBinding.errorOccurred(error) {
            viewModel.onEvent(ListFragmentEvent.ReloadData)
        }


    private fun onSuccessAction(data: ListFragmentState) {
        stateLoadingBinding.loadingFinished()
        listAdapter.submitList(data.noteItems)
        with(binding) {
            data.categories.initCategoriesChipGroup(chipgroupCategories) { categoryId ->
                viewModel.onEvent(
                    ListFragmentEvent.UpdateSelectedCategoryId(
                        categoryId
                    )
                )
            }
            Chip(context).setBtnAddCategoryStyle {
                findNavController().toAddCategoryDialog()
            }.also {
                chipgroupCategories.addView(it)
            }
        }
    }

    private fun initAdapter() {
        listAdapter.onNoteClick = { noteId ->
            findNavController().toDetailedNote(noteId)
        }
        listAdapter.onTodoClick = { todoId ->
            findNavController().toDetailedTodo(todoId)
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
