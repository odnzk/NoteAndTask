package com.noteapp.feature_todolist.api

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.domain.model.Todo
import com.noteapp.core.state.handleState
import com.noteapp.feature_todolist.R
import com.noteapp.feature_todolist.databinding.FragmentTodosListBinding
import com.noteapp.feature_todolist.internal.list.ListTodoEvent
import com.noteapp.feature_todolist.internal.list.ListTodoViewModel
import com.noteapp.feature_todolist.internal.navigation.toAddTodoBottomSheetDialog
import com.noteapp.feature_todolist.internal.navigation.toDetailedTodo
import com.noteapp.feature_todolist.internal.navigation.toTodoFiltersDialog
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

    private val viewModel by activityViewModels<ListTodoViewModel>()
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
            btnFilters.setOnClickListener {
                findNavController().toTodoFiltersDialog()
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
                    Log.d("hello", "$state")
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
