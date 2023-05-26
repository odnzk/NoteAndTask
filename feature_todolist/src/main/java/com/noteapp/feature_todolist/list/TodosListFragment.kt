package com.noteapp.feature_todolist.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.domain.model.Todo
import com.noteapp.feature_todolist.R
import com.noteapp.feature_todolist.databinding.FragmentTodosListBinding
import com.noteapp.feature_todolist.util.navigation.navigateToAddTodoBottomSheetDialog
import com.noteapp.feature_todolist.util.navigation.navigateToDetailedTodo
import com.noteapp.feature_todolist.util.navigation.navigateToTodoFiltersDialog
import com.noteapp.ui.BaseFragment
import com.noteapp.ui.collectAsUiState
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.HandledError
import com.noteapp.ui.ext.initStandardVerticalRecyclerView
import com.noteapp.ui.ext.loadingFinished
import com.noteapp.ui.ext.loadingStarted
import com.noteapp.ui.ext.onError
import com.noteapp.ui.ext.showSnackbar
import com.noteapp.ui.recycler.SwipeCallback
import com.noteapp.ui.recycler.todo.TodoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class TodosListFragment : BaseFragment() {
    private var _binding: FragmentTodosListBinding? = null
    private val binding: FragmentTodosListBinding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val viewModel by activityViewModels<ListTodoViewModel>()
    private val todosAdapter = TodoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosListBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        initUI()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        viewModel.onEvent(ListTodoEvent.SaveTodoFilters)
    }

    private fun onError(handledError: HandledError) {
        stateLoadingBinding.onError(handledError.message) {
            viewModel.onEvent(ListTodoEvent.Reload)
        }
    }

    private fun onSuccess(notes: List<Todo>) {
        stateLoadingBinding.loadingFinished()
        todosAdapter.submitList(notes)
    }

    override fun initUI() = with(binding.recyclerViewNotes) {
        val itemTouchHelper =
            ItemTouchHelper(SwipeCallback(todosAdapter) { removedItem ->
                viewModel.onEvent(ListTodoEvent.DeleteTodo(removedItem as Todo))
                showSnackbar(R.string.success_delete) {
                    viewModel.onEvent(ListTodoEvent.RestoreTodo)
                }
            })
        initStandardVerticalRecyclerView(itemTouchHelper)
        adapter = todosAdapter
    }


    override fun setupListeners() {
        with(binding) {
            btnAdd.setOnClickListener { navigateToAddTodoBottomSheetDialog() }
            btnFilters.setOnClickListener { navigateToTodoFiltersDialog() }
        }
    }

    override fun observeState() {
        lifecycleScope.launch {
            viewModel.todos.collectAsUiState(
                context,
                lifecycleOwner = viewLifecycleOwner,
                onSuccess = ::onSuccess,
                onError = ::onError,
                onLoading = stateLoadingBinding::loadingStarted
            )
        }
    }

    private fun initAdapter() = todosAdapter.run {
        onTodoClick = { todoId -> navigateToDetailedTodo(todoId) }
        onCheckboxClick = { todoId, isCompleted ->
            viewModel.onEvent(ListTodoEvent.UpdateTodoCompletedStatus(todoId, isCompleted))
        }
        submitList(emptyList())
    }

}
