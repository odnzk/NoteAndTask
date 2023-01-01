package com.example.noteapp.ui.dialogs.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.example.domain.model.Todo
import com.example.noteapp.databinding.BottomSheetAddTodoBinding
import com.example.noteapp.notifications.NotificationWorker
import com.example.noteapp.ui.fragments.list.ListFragmentEvent
import com.example.noteapp.ui.util.ext.showDatePicker
import com.example.noteapp.ui.util.ext.showDateTimePicker
import com.example.noteapp.ui.fragments.list.ListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AddTodoBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding: BottomSheetAddTodoBinding get() = _binding!!

    private val viewModel by viewModels<ListViewModel>()
    private val currentTodo: Todo = Todo.defaultInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnAdd.setOnClickListener {
                tilTitle.editText?.text?.let { currentTodo.title = it.toString() }
                viewModel.onEvent(ListFragmentEvent.AddItem(currentTodo))
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
                // todo show spinner??
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

    private fun createReminderNotification() {
        context?.showDateTimePicker { calendar ->
            // todo check todoItem validity
            val notificationWorkRequest: WorkRequest =
                OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInputData(
                        workDataOf(
                            NotificationWorker.NOTIFICATION_TITLE_KEY to "",
                            NotificationWorker.NOTIFICATION_CONTENT_KEY to ""
                        )
                    )
                    .build()
            // 1) set constraints  or .setInitialDelay(10, TimeUnit.MINUTES)
            // 2) set information about todoItem to Worker
            WorkManager.getInstance(requireContext()).enqueue(notificationWorkRequest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
