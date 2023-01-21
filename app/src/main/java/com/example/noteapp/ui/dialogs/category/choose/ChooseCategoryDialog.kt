package com.example.noteapp.ui.dialogs.category.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.noteapp.databinding.DialogChangeCategoryBinding
import com.example.noteapp.ui.util.CategoryOwnerType
import com.example.noteapp.ui.util.ext.toChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChooseCategoryDialog : DialogFragment() {
    private var _binding: DialogChangeCategoryBinding? = null
    private val binding: DialogChangeCategoryBinding get() = _binding!!

    private val viewModel: ChooseCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNoteItem()
    }

    private fun observeNoteItem() {
        lifecycleScope.launchWhenResumed {
            viewModel.uiCategoryList.collectLatest { uiCategories ->
                with(binding) {
                    if (uiCategories.isEmpty()) {
                        tvEmptyCategories.isVisible = true
                    } else {
                        tvEmptyCategories.isVisible = false
                        chipgroupCategories.isSingleSelection =
                            viewModel.type == CategoryOwnerType.TODO_TYPE
                        uiCategories.toChipGroup(chipgroupCategories) { categoryId ->
                            viewModel.onEvent(ChooseCategoryEvent.AddNoteItemCategory(categoryId))
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
        _binding = DialogChangeCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
