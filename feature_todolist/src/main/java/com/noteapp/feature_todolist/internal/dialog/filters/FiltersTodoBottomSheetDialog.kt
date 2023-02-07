package com.noteapp.feature_todolist.internal.dialog.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.TodoFilterAdditionalConditions
import com.example.domain.model.TodoFilterPeriod
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.noteapp.feature_todolist.R
import com.noteapp.feature_todolist.databinding.BottomSheetTodoFiltersBinding
import com.noteapp.feature_todolist.internal.list.ListTodoEvent
import com.noteapp.feature_todolist.internal.list.ListTodoViewModel
import com.noteapp.ui.ext.toChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FiltersTodoBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetTodoFiltersBinding? = null
    private val binding: BottomSheetTodoFiltersBinding get() = _binding!!

    private val viewModel by activityViewModels<ListTodoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFilters()
        initClickListeners()

    }

    private fun initClickListeners() {
        with(binding) {
            tgTaskFilterPeriod.addOnButtonCheckedListener { group, checkedId, _ ->
                val newPeriod = if (group.checkedButtonId != -1) {
                    when (checkedId) {
                        R.id.btn_filter_today -> TodoFilterPeriod.TODAY
                        R.id.btn_filter_tomorrow -> TodoFilterPeriod.TOMORROW
                        else -> TodoFilterPeriod.THIS_WEEK
                    }
                } else {
                    TodoFilterPeriod.NO_PERIOD
                }
                viewModel.onEvent(ListTodoEvent.UpdateTodoFilterPeriod(newPeriod))
            }
            cbHideCompleted.setOnCheckedChangeListener { _, _ ->
                viewModel.onEvent(
                    ListTodoEvent.UpdateTodoAdditionalFilters(
                        TodoFilterAdditionalConditions.HIDE_COMPLETED
                    )
                )
            }
            cbHideWithoutDeadline.setOnCheckedChangeListener { _, _ ->
                viewModel.onEvent(
                    ListTodoEvent.UpdateTodoAdditionalFilters(
                        TodoFilterAdditionalConditions.HIDE_WITHOUT_DEADLINE
                    )
                )
            }
        }
    }

    private fun observeFilters() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoFilters.collectLatest { filters ->
                    with(filters) {
                        categories.toChipGroup(
                            binding.chipgroupCategories
                        ) { selectedCategoryId ->
                            viewModel.onEvent(
                                ListTodoEvent.UpdateSelectedCategoriesId(
                                    selectedCategoryId
                                )
                            )
                        }

                        with(binding) {
                            when (period) {
                                TodoFilterPeriod.NO_PERIOD -> {}
                                TodoFilterPeriod.TODAY -> btnFilterToday.isChecked = true
                                TodoFilterPeriod.TOMORROW -> btnFilterTomorrow.isChecked = true
                                TodoFilterPeriod.THIS_WEEK -> btnFilterWeek.isChecked = true
                            }
                            additionalConditions.forEach { condition ->
                                when (condition) {
                                    TodoFilterAdditionalConditions.HIDE_COMPLETED -> cbHideCompleted.isChecked =
                                        true
                                    TodoFilterAdditionalConditions.HIDE_WITHOUT_DEADLINE ->
                                        cbHideWithoutDeadline.isChecked =
                                            true
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTodoFiltersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
