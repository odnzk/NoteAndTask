package com.example.mainlist.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.domain.model.NoteItem
import com.example.domain.model.NoteItemFilter
import com.example.feature_mainlist.databinding.FragmentListBinding
import com.example.mainlist.utils.navigateToAddCategoryDialog
import com.example.mainlist.utils.navigateToDetailedNote
import com.example.mainlist.utils.navigateToDetailedTodo
import com.example.mainlist.utils.recyler.NoteItemAdapter
import com.noteapp.ui.BaseFragment
import com.noteapp.ui.R
import com.noteapp.ui.collectAsUiState
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.HandledError
import com.noteapp.ui.ext.initStandardVerticalRecyclerView
import com.noteapp.ui.ext.loadingFinished
import com.noteapp.ui.ext.loadingStarted
import com.noteapp.ui.ext.onError
import com.noteapp.ui.ext.showSnackbar
import com.noteapp.ui.ext.toChipGroup
import com.noteapp.ui.observeWithLifecycle
import com.noteapp.ui.recycler.SwipeCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListFragment : BaseFragment() {
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupListeners()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }


    private fun initRecyclerView() = with(binding.recyclerView) {
        val itemTouchHelper = ItemTouchHelper(SwipeCallback(listAdapter) { removedItem ->
            viewModel.onEvent(ListFragmentEvent.DeleteItem(removedItem as NoteItem))
            showSnackbar(R.string.success_delete) {
                viewModel.onEvent(ListFragmentEvent.RestoreItem)
            }
        })
        initStandardVerticalRecyclerView(itemTouchHelper)
        adapter = listAdapter
    }

    private fun initAdapter() = listAdapter.run {
        onNoteClick = { noteId -> navigateToDetailedNote(noteId) }
        onTodoClick = { todoId -> navigateToDetailedTodo(todoId) }
        onTodoCheckboxClick = { id, isCompleted ->
            viewModel.onEvent(ListFragmentEvent.UpdateTodoCompletedStatus(id, isCompleted))
        }
        submitList(emptyList())
    }

    override fun initUI() {
        initRecyclerView()
    }

    override fun setupListeners() {
        binding.spinnerFilter.onItemSelectedListener = createOnItemSelectedListener()
        binding.svFindByTitle.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?) = false

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { viewModel.onEvent(ListFragmentEvent.UpdateSearchQuery(it)) }
                return false
            }
        })
        binding.btnClearAll.setOnClickListener { viewModel.onEvent(ListFragmentEvent.ClearAll) }
    }

    override fun observeState() {
        lifecycleScope.launch {
            viewModel.list.collectAsUiState(
                context,
                viewLifecycleOwner,
                onSuccess = ::onSuccess,
                onError = ::onError,
                onLoading = stateLoadingBinding::loadingStarted
            )
        }
        lifecycleScope.launch {
            viewModel.categories.observeWithLifecycle(viewLifecycleOwner) { categories ->
                categories.toChipGroup(binding.chipgroupCategories,
                    isCheckedStyleEnabled = true,
                    onLongClickListener = { categoryId ->
                        viewModel.onEvent(ListFragmentEvent.DeleteCategory(categoryId))
                        true
                    },
                    onAddCategoryClick = { navigateToAddCategoryDialog() }) { categoryId ->
                    viewModel.onEvent(ListFragmentEvent.UpdateSelectedCategoriesId(categoryId))
                }
            }
        }
    }

    private fun onError(handledError: HandledError) =
        stateLoadingBinding.onError(handledError.message) {
            viewModel.onEvent(ListFragmentEvent.Reload)
        }


    private fun onSuccess(data: List<NoteItem>) {
        stateLoadingBinding.loadingFinished()
        listAdapter.submitList(data)
    }

    private fun createOnItemSelectedListener() = object : OnItemSelectedListener {
        override fun onItemSelected(
            adapter: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            var spinnerItem = adapter?.getItemAtPosition(position)
            if (spinnerItem is String) {
                spinnerItem = spinnerItem.lowercase()
                NoteItemFilter.values().iterator().forEach { filter ->
                    if (spinnerItem == filter.key) {
                        viewModel.onEvent(ListFragmentEvent.UpdateFilter(filter))
                    }
                }
            }
        }

        override fun onNothingSelected(adapter: AdapterView<*>?) = Unit
    }

}
