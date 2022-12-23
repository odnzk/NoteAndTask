package com.example.noteapp.ui.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.domain.model.Todo
import com.example.noteapp.databinding.BottomSheetAddTodoBinding
import com.example.noteapp.ui.viewmodel.MainViewModel
import com.example.noteapp.ui.viewmodel.NoteItemEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddTodoBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding: BottomSheetAddTodoBinding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    private val currentTodo: Todo = Todo.defaultTodo()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnAdd.setOnClickListener {
                tilTitle.editText?.text?.let { currentTodo.title = it.toString() }
                viewModel.onEvent(NoteItemEvent.AddItem(currentTodo))
                this@AddTodoBottomSheetDialog.dismiss()
            }

            btnSetDeadline.setOnClickListener {
                showDatePicker()
            }
            btnSetReminder.setOnClickListener {
                // show time picker
            }
            btnSetRepeating.setOnClickListener {
                // ??? new dialog...
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog =
            DatePickerDialog(
                requireContext(),
                { datePicker, year, month, day ->
                    val date = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, day)
                    }
                    // set deadline date
                    currentTodo.deadlineDate = Date(date.timeInMillis)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
