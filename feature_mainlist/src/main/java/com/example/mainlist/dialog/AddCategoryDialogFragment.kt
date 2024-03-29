package com.example.mainlist.dialog

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
import com.example.feature_mainlist.databinding.DialogAddCategoryBinding
import com.noteapp.ui.collectAsUiState
import com.noteapp.ui.ext.HandledError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class AddCategoryDialogFragment : DialogFragment() {
    private var _binding: DialogAddCategoryBinding? = null
    private val binding: DialogAddCategoryBinding get() = _binding!!
    private val viewModel: AddCategoryDialogViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.category.collectAsUiState(
            context,
            viewLifecycleOwner,
            onComplete = { dismiss() },
            onError = ::onError,
            onProgress = {}
        )
    }

    private fun onError(handledError: HandledError) {
        binding.etCategoryTitle.error = handledError.message
    }

    private fun setupClickListeners() = with(binding) {
        btnAddNewCategory.setOnClickListener {
            rgColors.children
                .filterIsInstance<RadioButton>()
                .find { it.id == rgColors.checkedRadioButtonId }
                ?.let { selectedRb ->
                    selectedRb.buttonTintList?.defaultColor?.let { color ->
                        addCategory(color)
                    }
                }
        }
    }

    private fun addCategory(categoryColor: Int) = lifecycleScope.launch {
        viewModel.onEvent(
            AddCategoryDialogEvent.AddCategory(
                Category(title = binding.etCategoryTitle.text.toString(), color = categoryColor)
            )
        )
    }

}
