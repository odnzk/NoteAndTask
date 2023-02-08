package com.example.mainlist.internal.dialog

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.Category
import com.example.domain.validation.CategoryValidator
import com.example.feature_mainlist.databinding.DialogAddCategoryBinding
import com.example.noteapp.ui.util.exceptions.InvalidCategoryException
import com.noteapp.ui.R
import com.noteapp.ui.collectAsUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class AddCategoryDialog : DialogFragment() {
    private var _binding: DialogAddCategoryBinding? = null
    private val binding: DialogAddCategoryBinding get() = _binding!!

    private val viewModel: AddCategoryDialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        observeState()
    }

    private fun observeState() =
        lifecycleScope.launch {
            viewModel.category.collectAsUiState(
                viewLifecycleOwner,
                onComplete = { dismiss() },
                onError = ::showError,
                onProgress = {} // todo maybe remove Progress state
            )
        }

    private fun showError(throwable: Throwable) {
        val message = when (throwable) {
            is InvalidCategoryException -> getString(
                R.string.error_invalid_category_title,
                CategoryValidator.MIN_LENGTH,
                CategoryValidator.MAX_LENGTH
            )
            is SQLiteConstraintException -> getString(R.string.error_category_with_this_title_already_exist)
            else -> getString(R.string.error_unknown, throwable.message.orEmpty())
        }
        binding.etCategoryTitle.error = message
        // todo error handling, why displaying in etCategoryTitle??
    }

    private fun initClickListeners() {
        with(binding) {
            btnAddNewCategory.setOnClickListener {
                val selectedId = rgColors.checkedRadioButtonId
                rgColors.children.filterIsInstance<RadioButton>().find { it.id == selectedId }
                    ?.let { selectedRadioButton ->
                        selectedRadioButton.buttonTintList?.defaultColor?.let { color ->
                            lifecycleScope.launch {
                                viewModel.onEvent(
                                    AddCategoryDialogEvent.AddCategory(
                                        Category(
                                            title = etCategoryTitle.text.toString(), color = color
                                        )
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
