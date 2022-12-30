package com.example.noteapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository
import com.example.noteapp.R
import com.example.noteapp.databinding.DialogAddCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddCategoryDialog : DialogFragment(R.layout.dialog_add_category) {
    private var _binding: DialogAddCategoryBinding? = null
    private val binding: DialogAddCategoryBinding get() = _binding!!

    @Inject
    lateinit var categoryRepository: CategoryRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnAddNewCategory.setOnClickListener {
                val title = etCategoryTitle.text.toString()
                if (title.isEmpty() || title.isBlank()) {
                    etCategoryTitle.error = getString(R.string.error_empty_or_blanc_category_title)
                } else {
                    // todo children filtering??
                    val selectedId = rgColors.checkedRadioButtonId
                    rgColors.children.filterIsInstance<RadioButton>().find { it.id == selectedId }
                        ?.let { selectedRadioButton ->
                            // todo check all dispatchers
                            // -8286721
                            val colorRgbInteger: Int? =
                                selectedRadioButton.buttonTintList?.defaultColor
                            lifecycleScope.launch {
                                // todo color:String to color:Int
                                colorRgbInteger?.let {
                                    categoryRepository.add(
                                        Category(title = title, color = colorRgbInteger)
                                    )
                                }
                                dismiss()
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
        _binding = DialogAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
