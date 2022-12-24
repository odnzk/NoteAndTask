package com.example.noteapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import com.example.domain.model.Todo
import com.example.noteapp.databinding.BottomSheetAddTodoBinding
import com.example.noteapp.ui.fragments.Events.ListFragmentEvent
import com.example.noteapp.ui.util.ext.showDatePicker
import com.example.noteapp.ui.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddTodoBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding: BottomSheetAddTodoBinding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    private val currentTodo: Todo = Todo.defaultInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnAdd.setOnClickListener {
                tilTitle.editText?.text?.let { currentTodo.title = it.toString() }
                viewModel.onEvent(ListFragmentEvent.AddItem(currentTodo))
                this@AddTodoBottomSheetDialog.dismiss()
            }

            btnSetDeadline.setOnClickListener {
                context?.showDatePicker(::setDeadlineDate)
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

    private fun setDeadlineDate(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        // set deadline date
        currentTodo.deadlineDate = Date(date.timeInMillis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
