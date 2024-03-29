package com.study.feature_detailed_screen.fragments.todo.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.Todo
import com.example.domain.util.exceptions.InvalidNoteException
import com.example.domain.util.exceptions.NotUniqueFieldException
import com.example.noteapp.ui.util.ext.showDatePicker
import com.google.android.material.chip.Chip
import com.noteapp.ui.BaseFragment
import com.noteapp.ui.R
import com.noteapp.ui.collectAsUiState
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.*
import com.study.feature_detailed_screen.databinding.FragmentDetailedTodoBinding
import com.study.feature_detailed_screen.navigation.navigateToChooseCategoryDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class TodoDetailedFragment : BaseFragment() {
    private var _binding: FragmentDetailedTodoBinding? = null
    private val binding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val viewModel: TodoDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedTodoBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }

    override fun initUI() = Unit

    override fun setupListeners() {
        with(binding) {
            btnDelete.setOnClickListener {
                viewModel.onEvent(TodoDetailedEvent.DeleteTodo)
                binding.root.showSnackbar(getString(R.string.success_delete))
            }
            etTitle.doAfterTextChanged {
                viewModel.todo.value.data?.let { todo ->
                    val updatedTodo = todo.copy(title = etTitle.text.toString())
                    viewModel.onEvent(TodoDetailedEvent.UpdateTodo(updatedTodo))
                }
            }
            btnChangeDeadlineDate.setOnClickListener { context?.showDatePicker(::setDeadlineDate) }
        }
    }

    override fun observeState() {
        lifecycleScope.launch {
            viewModel.todo.collectAsUiState(
                context,
                viewLifecycleOwner,
                onSuccess = ::onSuccess,
                onLoading = stateLoadingBinding::loadingStarted,
                onError = ::onError
            )
        }
    }

    private fun onError(handlerError: HandledError) {
        when (handlerError.error) {
            is InvalidNoteException -> binding.etTitle.error = handlerError.message
            is NotUniqueFieldException -> binding.etTitle.error = handlerError.message
            else -> stateLoadingBinding.onError(handlerError.message) {
                viewModel.onEvent(TodoDetailedEvent.Reload)
            }
        }
    }

    private fun onSuccess(todo: Todo) {
        stateLoadingBinding.loadingFinished()
        with(binding) {
            todo.run {
                etTitle.setText(title)
                cbIsCompleted.isChecked = isCompleted
                cbIsCompleted.setCategoryColor(category)

                deadlineDate?.let {
                    btnChangeDeadlineDate.text = it.formatToTodoDate()
                }

                chipgroupCategory.removeAllViews()
                category?.let {
                    chipgroupCategory.addView(
                        it.toChipCategory(requireContext(), isCheckedStyleEnabled = false) {
                           navigateToChooseCategoryDialog(viewModel.todoId)
                        })
                } ?: chipgroupCategory.addView(Chip(context).setBtnAddCategoryStyle {
                        navigateToChooseCategoryDialog(viewModel.todoId)
                    })
                reminderCalendar?.let {
                    btnChangeReminderTime.text = it.formatToReminderString()
                }
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

}
