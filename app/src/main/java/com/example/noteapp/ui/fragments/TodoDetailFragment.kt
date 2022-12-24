package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.Todo
import com.example.noteapp.databinding.FragmentDetailedTodoBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.ext.convertTUiString
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
import com.example.noteapp.ui.viewmodel.TodoDetailsViewModel
import com.example.noteapp.ui.viewmodel.handleState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoDetailFragment : Fragment() {
    private var _binding: FragmentDetailedTodoBinding? = null
    private val binding get() = _binding!!

    private val stateLoadingBinding by lazy {
        StateLoadingBinding.bind(binding.root)
    }

    private val viewModel: TodoDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTodo()
    }

    private fun initTodo() {
        lifecycleScope.launch {
            viewModel.todo.collect { state ->
                state.handleState(
                    onLoadingAction = stateLoadingBinding::loadingStarted,
                    onSuccessAction = ::showTodo,
                    onErrorAction = ::errorOccurred
                )
            }
        }
    }

    private fun errorOccurred(error: Throwable) {
        stateLoadingBinding.errorOccurred(error) { viewModel.loadData() }
    }

    private fun showTodo(todo: Todo) {
        stateLoadingBinding.loadingFinished()
        with(binding) {
            tvTitle.text = todo.title
            rbIsCompleted.isChecked = todo.isCompleted
            todo.deadlineDate?.let {
                btnChangeDeadlineDate.text = it.convertTUiString()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
