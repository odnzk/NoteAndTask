package com.noteapp.feature_todolist.internal.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.DatePicker
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.validation.TodoValidator
import com.example.noteapp.ui.util.exceptions.InvalidTodoException
import com.example.noteapp.ui.util.ext.showDatePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.noteapp.core.state.CompletableState
import com.noteapp.feature_todolist.databinding.BottomSheetAddTodoBinding
import com.noteapp.feature_todolist.internal.TodoDeadline
import com.noteapp.feature_todolist.internal.TodoPeriodicity
import com.noteapp.feature_todolist.internal.TodoReminder
import com.noteapp.ui.R
import com.noteapp.ui.ext.initValues
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.single
import java.util.*


@AndroidEntryPoint
class AddTodoBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding: BottomSheetAddTodoBinding get() = _binding!!

    private val viewModel by viewModels<AddTodoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddTodoBinding.inflate(inflater, container, false)

        observeState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCategoriesSpinner()
        initDeadlineSpinner()
        initReminderSpinner()
        initPeriodicitySpinner()

        initClickListeners()
    }

    private fun initPeriodicitySpinner() {
        val periodicityTitles: Array<String> =
            TodoPeriodicity.values().map { getString(it.titleId) }.toTypedArray()
        binding.spinnerReminder.initValues(periodicityTitles)
        binding.spinnerReminder.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, p1: View?, pos: Int, id: Long
            ) {
                viewModel.onEvent(AddTodoDialogEvent.UpdatePeriodInfo(TodoPeriodicity.values()[pos]))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }
    }

    private fun initReminderSpinner() {
        val reminderTitles: Array<String> =
            TodoReminder.values().map { getString(it.titleId) }.toTypedArray()
        binding.spinnerReminder.initValues(reminderTitles)
        binding.spinnerReminder.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, p1: View?, pos: Int, id: Long
            ) {
                val variant = TodoReminder.values()[pos]
                // todo load into worker?
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }
    }

    private fun initDeadlineSpinner() {
        val deadlineTitles: Array<String> =
            TodoDeadline.values().map { getString(it.titleId) }.toTypedArray()
        binding.spinnerDeadline.initValues(deadlineTitles)
        binding.spinnerDeadline.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, p1: View?, pos: Int, id: Long
            ) {
                when (TodoDeadline.values()[pos]) {
                    TodoDeadline.NO_DEADLINE -> {}
                    TodoDeadline.TODAY -> viewModel.onEvent(
                        AddTodoDialogEvent.UpdateDeadlineDate(
                            Date()
                        )
                    )
                    TodoDeadline.TOMORROW -> Calendar.getInstance().also {
                        it.add(Calendar.DAY_OF_MONTH, 1)
                        viewModel.onEvent(AddTodoDialogEvent.UpdateDeadlineDate(Date(it.timeInMillis)))
                    }
                    TodoDeadline.CUSTOM -> {
                        context?.showDatePicker { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                            val date = Calendar.getInstance().apply {
                                set(Calendar.YEAR, year)
                                set(Calendar.MONTH, month)
                                set(Calendar.DAY_OF_MONTH, day)
                            }
                            viewModel.onEvent(AddTodoDialogEvent.UpdateDeadlineDate(Date(date.timeInMillis)))
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }
    }

    private fun initClickListeners() {
        with(binding) {
            btnAdd.setOnClickListener {
                viewModel.onEvent(AddTodoDialogEvent.AddTodo)
            }
            tilTitle.editText?.doOnTextChanged { text, start, before, count ->
                viewModel.onEvent(
                    AddTodoDialogEvent.UpdateTitle(
                        text.toString()
                    )
                )
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenResumed {
            viewModel.currentTodo.collectLatest { state ->
                when (state) {
                    is CompletableState.Completed -> dismiss()
                    is CompletableState.Error -> {
                        state.error?.let { error ->
                            if (error is InvalidTodoException) {
                                binding.tilTitle.error =
                                    getString(
                                        R.string.error_invalid_todo_title,
                                        TodoValidator.MIN_LENGTH,
                                        TodoValidator.MAX_LENGTH
                                    )
                            }
                        }
                    }
                    is CompletableState.InProgress -> {
                        // todo
                    }
                }
            }
        }
    }

    private fun initCategoriesSpinner() {
        lifecycleScope.launchWhenResumed {
            val categories = viewModel.categories.single()
            if (categories.isEmpty()) {
                binding.spinnerCategories.visibility = View.GONE
                cancel()
                return@launchWhenResumed
            }
            val categoriesTitleArray =
                categories.map { category -> category.title }.toMutableList().apply {
                        add(0, getString(com.noteapp.ui.R.string.spinner_no_category))
                    }.toTypedArray()

            with(binding.spinnerCategories) {
                initValues(
                    categoriesTitleArray
                )
                onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, p1: View?, pos: Int, id: Long
                    ) {
                        (parent?.getItemAtPosition(pos) as? String)?.let { categoryTitle ->
                            if (categoryTitle == getString(R.string.spinner_no_category)) return
                            categories.forEach { category ->
                                if (category.title == categoryTitle) {
                                    viewModel.onEvent(AddTodoDialogEvent.UpdateCategory(category))
                                }
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) = Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
