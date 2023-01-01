package com.example.noteapp.ui.fragments.todo.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.Todo
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentTodosListBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.recycler.todo.TodoAdapter
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.ext.initStandardVerticalRecyclerView
import com.example.noteapp.ui.util.handleState
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
import dagger.hilt.android.AndroidEntryPoint

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

        with(binding) {
            btnAdd.setOnClickListener {
                findNavController().navigate(R.id.action_todosListFragment_to_addTodoBottomSheetDialog)
            }
            btnClearAll.setOnClickListener {
                viewModel.onEvent(ListTodoEvent.ClearAll)
            }
        }
    }

    private fun initRecyclerView() =
        binding.recyclerViewNotes.run {
            initStandardVerticalRecyclerView()
            adapter = todosAdapter
        }

    private fun observeTodos() =
        lifecycleScope.launchWhenStarted {
            viewModel.todos.collect { state ->
                state.handleState(
                    onLoadingAction = stateLoadingBinding::loadingStarted,
                    onSuccessAction = ::showTodos,
                    onErrorAction = ::showError,
                )
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
                val action =
                    TodosListFragmentDirections.actionTodosListFragmentToTodoDetailFragment(todoId)
                findNavController().navigate(action)
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
