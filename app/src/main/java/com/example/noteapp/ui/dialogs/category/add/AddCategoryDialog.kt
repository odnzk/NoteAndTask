package com.example.noteapp.ui.dialogs.category.add

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
import com.example.noteapp.R
import com.example.noteapp.databinding.DialogAddCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCategoryDialog : DialogFragment(R.layout.dialog_add_category) {
    private var _binding: DialogAddCategoryBinding? = null
    private val binding: DialogAddCategoryBinding get() = _binding!!

    private val viewModel: AddCategoryDialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
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
                            dismiss()
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
