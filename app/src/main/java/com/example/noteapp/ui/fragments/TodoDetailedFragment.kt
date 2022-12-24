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
import com.example.domain.model.Todo
import com.example.noteapp.databinding.FragmentDetailedTodoBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.fragments.Events.TodoDetailedEvent
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.ext.convertTUiString
import com.example.noteapp.ui.util.ext.insertToConstraintLayoutFlow
import com.example.noteapp.ui.util.ext.showDatePicker
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
import com.example.noteapp.ui.viewmodel.TodoDetailsViewModel
import com.example.noteapp.ui.viewmodel.handleState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class TodoDetailedFragment : Fragment() {
    private var _binding: FragmentDetailedTodoBinding? = null
    private val binding get() = _binding!!

    private val stateLoadingBinding by lazy {
        StateLoadingBinding.bind(binding.root)
    }

    private val viewModel: TodoDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTodo()
        with(binding) {

        }
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
            todo.run {
                etTitle.setText(title)
                cbIsCompleted.isChecked = isCompleted
                deadlineDate?.let {
                    btnChangeDeadlineDate.text = it.convertTUiString()
                }
                category.insertToConstraintLayoutFlow(flowCategories) {
                    // todo onCategoryClick: update category?
                }
            }
            // init listeners only if loading finished successfully
            btnDelete.setOnClickListener {
                viewModel.onEvent(TodoDetailedEvent.DeleteTodo)
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
