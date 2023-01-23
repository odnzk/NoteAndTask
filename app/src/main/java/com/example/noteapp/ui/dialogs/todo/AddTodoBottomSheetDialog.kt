package com.example.noteapp.ui.dialogs.todo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.noteapp.R
import com.example.noteapp.databinding.BottomSheetAddTodoBinding
import com.example.noteapp.ui.dialogs.CompletableState
import com.example.noteapp.ui.util.exceptions.InvalidTodoException
import com.example.noteapp.ui.util.ext.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
        observeState()
        init()
    }

    private fun init() {
        with(binding) {
            btnAdd.setOnClickListener {
                viewModel.onEvent(AddTodoDialogEvent.AddTodo)
            }
//            btnSetCategories.setOnClickListener {
//                // todo show spinner with existing categories
//            }
            btnSetDeadline.setOnClickListener {
                context?.showDatePicker { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                    val date = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, day)
                    }
                    viewModel.onEvent(AddTodoDialogEvent.UpdateDeadlineDate(Date(date.timeInMillis)))
                }
            }
            btnSetReminder.setOnClickListener {
                context?.showDateTimePicker { calendar ->
                    viewModel.onEvent(AddTodoDialogEvent.UpdateReminderInfo(calendar))
                }
            }
            btnSetRepeating.setOnClickListener {
                // todo show spinner
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
                                    getString(R.string.error_invalid_todo_title)
                            }
                        }
                    }
                    is CompletableState.InProgress -> {
                        with(binding) {
                            val todo = state.data
                            todo.deadlineDate?.let { btnSetDeadline.text = it.formatToTodoDate() }
                            todo.notificationCalendar?.let {
                                binding.btnSetReminder.text = it.formatToReminderString()
                            }
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
                        add(0, getString(R.string.spinner_no_category))
                    }.toTypedArray()
            binding.spinnerCategories.init(
                categoriesTitleArray, R.layout.item_spinner_row, R.id.tv_item_spinner
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
