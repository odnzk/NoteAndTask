package com.noteapp.feature_todolist.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.noteapp.core.state.handleState
import com.noteapp.feature_todolist.R
import com.noteapp.feature_todolist.databinding.FragmentTodosListBinding
import com.noteapp.feature_todolist.internal.list.ListTodoEvent
import com.noteapp.feature_todolist.internal.list.ListTodoViewModel
import com.noteapp.feature_todolist.internal.navigation.toAddTodoBottomSheetDialog
import com.noteapp.feature_todolist.internal.navigation.toDetailedTodo
import com.example.domain.model.Todo
import com.example.domain.model.TodoSortOrder
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.*
import com.noteapp.ui.recycler.todo.TodoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodosListFragment : Fragment() {
    private var _binding: FragmentTodosListBinding? = null
    private val binding: FragmentTodosListBinding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val viewModel: ListTodoViewModel by viewModels()
    private val todosAdapter = TodoAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeTodos()
        initRecyclerView()
        init()

    }

    private fun init() {
        with(binding) {
            btnAdd.setOnClickListener {
                findNavController().toAddTodoBottomSheetDialog()
            }
            btnClearAll.setOnClickListener {
                viewModel.onEvent(ListTodoEvent.ClearAll)
            }
            spinnerSort.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    p1: View?,
                    pos: Int,
                    id: Long
                ) {
                    val sortOrder = when (pos) {
                        1 -> TodoSortOrder.BY_DEADLINE
                        2 -> TodoSortOrder.TODAY
                        3 -> TodoSortOrder.THIS_WEEK
                        else -> TodoSortOrder.DEFAULT
                    }
                    viewModel.onEvent(ListTodoEvent.UpdateSortOrder(sortOrder))
                }

                override fun onNothingSelected(p0: AdapterView<*>?) = Unit

            }
        }
    }

    private fun initRecyclerView() =
        binding.recyclerViewNotes.run {
            val itemTouchHelper =
                ItemTouchHelper(com.noteapp.ui.recycler.SwipeCallback(todosAdapter) { removedItem ->
                    viewModel.onEvent(ListTodoEvent.DeleteItem(removedItem as Todo))
                    // undo listener
                    val listener = OnClickListener { viewModel.onEvent(ListTodoEvent.RestoreItem) }
                    showSnackbar(R.string.success_delete, listener)
                })
            initStandardVerticalRecyclerView(itemTouchHelper)
            adapter = todosAdapter
        }

    private fun observeTodos() =
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todos.collectLatest { state ->
                    state.handleState(
                        onLoadingAction = stateLoadingBinding::loadingStarted,
                        onSuccessAction = ::showTodos,
                        onErrorAction = ::showError,
                    )
                }
            }
        }

    private fun showError(throwable: Throwable) {
        stateLoadingBinding.errorOccurred(throwable) {
            viewModel.onEvent(ListTodoEvent.TryAgain)
        }
    }

    private fun showTodos(notes: List<Todo>) {
        stateLoadingBinding.loadingFinished()
        todosAdapter.submitList(notes)
    }

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

    private fun initAdapter() =
        todosAdapter.apply {
            onTodoClick = { todoId ->
                findNavController().toDetailedTodo(todoId)
            }
            onCheckboxClick = { todoId, isCompleted ->
                viewModel.onEvent(ListTodoEvent.UpdateTodoCompletedStatus(todoId, isCompleted))
            }
            submitList(emptyList())
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }
}
