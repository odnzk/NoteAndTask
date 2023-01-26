package com.noteapp.feature_detailedscreens.internal.dialog

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.noteapp.core.model.CategoryOwnerType
import com.noteapp.feature_detailedscreens.databinding.DialogChangeCategoryBinding
import com.noteapp.feature_detailedscreens.internal.ext.toChipGroup
import com.noteapp.ui.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ChooseCategoryDialog : DialogFragment() {
    private var _binding: DialogChangeCategoryBinding? = null
    private val binding: DialogChangeCategoryBinding get() = _binding!!

    private val viewModel: ChooseCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNoteItem()
    }

    private fun observeNoteItem() =
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiCategoryList.collectLatest { uiCategories ->
                    uiCategories.fold(
                        onSuccess = ::init,
                        onFailure = ::showError
                    )
                }
            }
        }

    private fun showError(error: Throwable) {
        with(binding.tvError) {
            val resString = when (error) {
                is NotFoundException -> R.string.error_not_found
                else -> R.string.error_unknown
            }
            text = getString(resString)
            isVisible = true
        }
    }

    private fun init(categoryList: List<UiCategory>) {
        with(binding) {
            if (categoryList.isEmpty()) {
                tvError.text = getString(R.string.state_categories_is_empty)
                tvError.isVisible = true
            } else {
                tvError.isVisible = false
                chipgroupCategories.isSingleSelection =
                    viewModel.type == CategoryOwnerType.TODO_TYPE
                categoryList.toChipGroup(chipgroupCategories) { categoryId ->
                    viewModel.onEvent(
                        ChooseCategoryEvent.AddNoteItemCategory(categoryId)
                    )
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
