package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.Todo
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentDetailedTodoBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.fragments.events.TodoDetailedEvent
import com.example.noteapp.ui.util.*
import com.example.noteapp.ui.util.ext.categoriesToFlowCategories
import com.example.noteapp.ui.util.ext.formatToTodoDate
import com.example.noteapp.ui.util.ext.showDatePicker
import com.example.noteapp.ui.util.ext.showSnackbar
import com.example.noteapp.ui.viewmodel.TodoDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TodoDetailedFragment : Fragment() {
    private var _binding: FragmentDetailedTodoBinding? = null
    private val binding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val viewModel: TodoDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTodo()
    }

    private fun initTodo() {
        lifecycleScope.launchWhenResumed {
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
        stateLoadingBinding.errorOccurred(error) { viewModel.onEvent(TodoDetailedEvent.TryLoadingTodoAgain) }
    }

    private fun showTodo(todo: Todo) {
        stateLoadingBinding.loadingFinished()
        with(binding) {
            todo.run {
                etTitle.setText(title)
                cbIsCompleted.isChecked = isCompleted
                deadlineDate?.let {
                    btnChangeDeadlineDate.text = it.formatToTodoDate()
                }
                categories.categoriesToFlowCategories(flowCategories) {
                    val action = TodoDetailedFragmentDirections
                        .actionTodoDetailFragmentToChooseCategoryDialog(
                            type = CategoryOwnerType.TODO_TYPE,
                            todoId = todo.id
                        )
                    findNavController().navigate(action)
                }
            }
            // init listeners only if loading finished successfully
            btnDelete.setOnClickListener {
                viewModel.onEvent(TodoDetailedEvent.DeleteTodo)
                binding.root.showSnackbar(getString(R.string.success_delete))
            }
            etTitle.doAfterTextChanged {
                viewModel.todo.value.data?.let { todo ->
                    viewModel.onEvent(TodoDetailedEvent.UpdateTodo(todo.copy(title = etTitle.text.toString())))
                }
            }
            btnChangeDeadlineDate.setOnClickListener {
                context?.showDatePicker(::setDeadlineDate)
            }
        }
    }

    private fun setDeadlineDate(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        viewModel.todo.value.data?.let { todo ->
            viewModel.onEvent(TodoDetailedEvent.UpdateTodo(todo.copy(deadlineDate = Date(date.timeInMillis))))
        }
        binding.btnChangeDeadlineDate.text = Date(date.timeInMillis).formatToTodoDate()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedTodoBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }
}