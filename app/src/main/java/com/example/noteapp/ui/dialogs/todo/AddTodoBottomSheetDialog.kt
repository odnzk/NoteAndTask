package com.example.noteapp.ui.dialogs.todo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import com.example.noteapp.R
import com.example.noteapp.databinding.BottomSheetAddTodoBinding
import com.example.noteapp.ui.util.ext.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AddTodoBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding: BottomSheetAddTodoBinding get() = _binding!!

    private val viewModel by viewModels<AddTodoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCategories()

        with(binding) {
            btnAdd.setOnClickListener {
                tilTitle.editText?.text?.let { viewModel.currentTodo.title = it.toString() }
                viewModel.addItem(viewModel.currentTodo)
                this@AddTodoBottomSheetDialog.dismiss()
            }

            btnSetCategories.setOnClickListener {
                // todo show spinner with existing categories
            }
            btnSetDeadline.setOnClickListener {
                context?.showDatePicker(::setDeadlineDate)
            }
            btnSetReminder.setOnClickListener {
                createReminderNotification()
            }
            btnSetRepeating.setOnClickListener {
                // todo show spinner
            }
        }
    }

    private fun observeCategories() {
        with(binding) {
            val categoriesTitleArray =
                viewModel.categories.map { category -> category.title }.toTypedArray()
            spinnerCategories.init(
                categoriesTitleArray, R.layout.item_spinner_row, R.id.tv_item_spinner
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setDeadlineDate(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        // set deadline date
        binding.btnSetDeadline.text = Date(date.timeInMillis).formatToTodoDate()
        viewModel.currentTodo.deadlineDate = Date(date.timeInMillis)
    }

    private fun createReminderNotification() {
        context?.showDateTimePicker { calendar ->
            binding.btnSetReminder.text = calendar.formatToReminderString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
