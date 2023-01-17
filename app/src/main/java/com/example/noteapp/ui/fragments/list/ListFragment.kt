package com.example.noteapp.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.domain.model.NoteItem
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentListBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.fragments.todo.list.ListTodoEvent
import com.example.noteapp.ui.recycler.SwipeCallback
import com.example.noteapp.ui.recycler.noteitem.NoteItemAdapter
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.ext.categoriesToFlowCategories
import com.example.noteapp.ui.util.ext.initStandardVerticalRecyclerView
import com.example.noteapp.ui.util.ext.showSnackbar
import com.example.noteapp.ui.util.handleState
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
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

        with(binding) {
            svFindByTitle.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    p0?.let {
                        viewModel.onEvent(ListFragmentEvent.UpdateSearchQuery(it))
                    }
                    return false
                }
            })
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
                val itemTouchHelper =
                    ItemTouchHelper(SwipeCallback(listAdapter) { removedItem ->
                        viewModel.onEvent(ListFragmentEvent.DeleteItem(removedItem as NoteItem))
                        // undo listener
                        val listener =
                            View.OnClickListener { viewModel.onEvent(ListFragmentEvent.RestoreItem) }
                        showSnackbar(R.string.success_delete, listener)
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
            data.categories.categoriesToFlowCategories(root, flowCategories) { id ->
                viewModel.onEvent(ListFragmentEvent.UpdateSelectedCategoryId(id))
            }
        }
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
