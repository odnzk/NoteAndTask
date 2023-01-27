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
import com.example.domain.model.TodoPeriod
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.noteapp.feature_todolist.R
import com.noteapp.feature_todolist.databinding.BottomSheetTodoFiltersBinding
import com.noteapp.feature_todolist.internal.list.ListTodoEvent
import com.noteapp.feature_todolist.internal.list.ListTodoViewModel
import com.noteapp.ui.ext.initCategoriesChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FiltersTodoBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetTodoFiltersBinding? = null
    private val binding: BottomSheetTodoFiltersBinding get() = _binding!!

    private val viewModel by activityViewModels<ListTodoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        initClickListeners()

    }

    private fun initClickListeners() {
        with(binding) {
            tgTaskFilterPeriod.addOnButtonCheckedListener { group, checkedId, _ ->
                val newPeriod = if (group.checkedButtonId != -1) {
                    when (checkedId) {
                        R.id.btn_filter_today -> TodoPeriod.TODAY
                        R.id.btn_filter_tomorrow -> TodoPeriod.TOMORROW
                        else -> TodoPeriod.THIS_WEEK
                    }
                } else {
                    TodoPeriod.NO_PERIOD
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

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.distinctUntilChanged().collectLatest { categories ->
                    categories.initCategoriesChipGroup(binding.chipgroupCategories){

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
