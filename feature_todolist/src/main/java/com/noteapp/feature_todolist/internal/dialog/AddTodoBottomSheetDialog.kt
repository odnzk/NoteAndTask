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
import com.example.noteapp.ui.util.exceptions.InvalidTodoException
import com.example.noteapp.ui.util.ext.showDatePicker
import com.example.noteapp.ui.util.ext.showDateTimePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.noteapp.core.state.CompletableState
import com.noteapp.feature_todolist.databinding.BottomSheetAddTodoBinding
import com.noteapp.ui.R
import com.noteapp.ui.ext.formatToReminderString
import com.noteapp.ui.ext.initClickListeners
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.single
import java.util.*


@AndroidEntryPoint
class AddTodoBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding: BottomSheetAddTodoBinding get() = _binding!!

    private val viewModel by viewModels<AddTodoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCategoriesSpinner()
        initDeadlineSpinner()
        observeState()
        init()
    }

    private fun initDeadlineSpinner() {
        binding.spinnerDeadline.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, p1: View?, pos: Int, id: Long
            ) {
                when (pos) {
                    1 -> {
                        // today
                        viewModel.onEvent(AddTodoDialogEvent.UpdateDeadlineDate(Date()))
                    }
                    2 -> {
                        // tomorrow
                        Calendar.getInstance().also {
                            it.add(Calendar.DAY_OF_MONTH, 1)
                            viewModel.onEvent(AddTodoDialogEvent.UpdateDeadlineDate(Date(it.timeInMillis)))
                        }
                    }
                    3 -> {
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

    private fun init() {
        with(binding) {
            btnAdd.setOnClickListener {
                viewModel.onEvent(AddTodoDialogEvent.AddTodo)
            }
            btnSetReminder.setOnClickListener {
                context?.showDateTimePicker { calendar ->
                    viewModel.onEvent(
                        AddTodoDialogEvent.UpdateReminderInfo(
                            calendar
                        )
                    )
                }
            }
            btnSetRepeating.setOnClickListener {
                // todo show spinner
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
                                    getString(com.noteapp.ui.R.string.error_invalid_todo_title)
                            }
                        }
                    }
                    is CompletableState.InProgress -> {
                        with(binding) {
                            val todo = state.data
                            todo.notificationCalendar?.let {
                                binding.btnSetReminder.text = it.formatToReminderString()
                            }
                            // todo set period
                        }
                    }
                }
            }
        }
    }

    private fun initCategoriesSpinner() {
        lifecycleScope.launchWhenResumed {
            val categories = viewModel.categories.single()
            val categoriesTitleArray =
                categories.map { category -> category.title }.toMutableList()
                    .apply {
                        add(0, getString(com.noteapp.ui.R.string.spinner_no_category))
                    }.toTypedArray()
            binding.spinnerCategories.initClickListeners(
                categoriesTitleArray
            )
            binding.spinnerCategories.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    pos: Int,
                    id: Long
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
