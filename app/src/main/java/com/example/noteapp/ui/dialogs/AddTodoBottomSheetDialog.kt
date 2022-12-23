package com.example.noteapp.ui.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import com.example.domain.model.Todo
import com.example.noteapp.R
import com.example.noteapp.databinding.BottomSheetAddTodoBinding
import com.example.noteapp.ui.viewmodel.MainViewModel
import com.example.noteapp.ui.viewmodel.NoteItemEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class AddTodoBottomSheetDialog : BottomSheetDialogFragment(R.layout.bottom_sheet_add_todo) {
    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding: BottomSheetAddTodoBinding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    private val currentTodo: Todo = Todo.defaultTodo()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BottomSheetAddTodoBinding.bind(view)

        with(binding) {
            btnAdd.setOnClickListener {
                tilTitle.editText?.text?.let { currentTodo.title = it.toString() }
                viewModel.onEvent(NoteItemEvent.AddItem(currentTodo))
            }

            btnSetDeadline.setOnClickListener {
                // todo show date picker
            }
            btnSetReminder.setOnClickListener {
                // todo show date picker
            }
            btnSetRepeating.setOnClickListener {
                // ??? new dialog...
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog: DatePickerDialog =
            DatePickerDialog(
                requireContext(),
                listener = object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

                    }
                },
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
