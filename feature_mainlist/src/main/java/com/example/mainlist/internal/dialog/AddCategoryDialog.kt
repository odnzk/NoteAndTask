package com.example.mainlist.internal.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.feature_mainlist.R
import com.example.feature_mainlist.databinding.DialogAddCategoryBinding
import com.example.noteapp.ui.util.exceptions.InvalidCategoryException
import com.noteapp.core.state.CompletableState
import com.example.domain.model.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class AddCategoryDialog : DialogFragment(R.layout.dialog_add_category) {
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.category.collectLatest { state ->
                    when (state) {
                        is CompletableState.Completed -> dismiss()
                        is CompletableState.Error -> if (state.error is InvalidCategoryException) {
                            binding.etCategoryTitle.error =
                                getString(com.noteapp.ui.R.string.error_invalid_category_title)
                        }
                        is CompletableState.InProgress -> {}
                    }
                }
            }
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
