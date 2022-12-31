package com.example.noteapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.noteapp.databinding.DialogChangeCategoryBinding
import com.example.noteapp.model.UiCategory
import com.example.noteapp.ui.util.handleState
import com.example.noteapp.viewmodel.ChooseCategoryViewModel
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
        lifecycleScope.launchWhenStarted {
            viewModel.noteItem.collectLatest { state ->
                state.handleState(
                    onLoadingAction = ::onLoadingState,
                    onErrorAction = ::onErrorState,
                    onSuccessAction = ::onSuccessState
                )
            }
        }
    }

    private fun onLoadingState() {

    }

    private fun onErrorState(error: Throwable) {
        //
    }

    private fun onSuccessState(uiCategoryList: List<UiCategory>) {
        with(binding) {
            //todo
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
