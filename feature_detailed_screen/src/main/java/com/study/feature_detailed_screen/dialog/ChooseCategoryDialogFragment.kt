package com.study.feature_detailed_screen.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.noteapp.core.model.CategoryOwnerType
import com.noteapp.ui.R
import com.noteapp.ui.ext.HandledError
import com.noteapp.ui.ext.toChipGroup
import com.noteapp.ui.model.UiCategory
import com.noteapp.ui.observeWithLifecycle
import com.study.feature_detailed_screen.databinding.DialogChangeCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ChooseCategoryDialogFragment : DialogFragment() {
    private var _binding: DialogChangeCategoryBinding? = null
    private val binding: DialogChangeCategoryBinding get() = _binding!!
    private val viewModel: ChooseCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangeCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNoteItem()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeNoteItem() =
        lifecycleScope.launch {
            viewModel.uiCategoryList.observeWithLifecycle(
                context,
                viewLifecycleOwner,
                onSuccess = ::onSuccess,
                onError = ::onError
            )
        }


    private fun onError(handledError: HandledError) {
        with(binding.tvError) {
            text = handledError.message
            isVisible = true
        }
    }

    private fun onSuccess(categoryList: List<UiCategory>) = with(binding) {
        if (categoryList.isEmpty()) {
            tvError.text = getString(R.string.state_categories_is_empty)
            tvError.isVisible = true
        } else {
            tvError.isVisible = false
            chipgroupCategories.isSingleSelection =
                viewModel.type == CategoryOwnerType.TODO_TYPE
            categoryList.toChipGroup(chipgroupCategories) { categoryId ->
                viewModel.onEvent(ChooseCategoryEvent.AddNoteItemCategory(categoryId))
            }
        }
    }

}
