package com.study.feature_detailed_screen.fragments.note.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.Note
import com.example.domain.util.exceptions.Field
import com.example.domain.util.exceptions.InvalidNoteException
import com.example.domain.util.exceptions.NotUniqueFieldException
import com.noteapp.ui.BaseFragment
import com.noteapp.ui.R
import com.noteapp.ui.collectAsUiState
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.HandledError
import com.noteapp.ui.ext.formatToNoteDate
import com.noteapp.ui.ext.loadingFinished
import com.noteapp.ui.ext.loadingStarted
import com.noteapp.ui.ext.onError
import com.noteapp.ui.ext.showSnackbar
import com.noteapp.ui.ext.toChipGroup
import com.noteapp.ui.observeWithLifecycle
import com.study.feature_detailed_screen.databinding.FragmentDetailedNoteBinding
import com.study.feature_detailed_screen.navigation.navigateToChooseCategoryDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteDetailedFragment : BaseFragment() {
    private var _binding: FragmentDetailedNoteBinding? = null
    private val binding get() = _binding!!
    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!
    private val viewModel: NoteDetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedNoteBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }

    override fun initUI() = Unit

    override fun setupListeners() = with(binding) {
        btnDelete.setOnClickListener {
            viewModel.onEvent(NoteDetailedEvent.DeleteNote)
            root.showSnackbar(getString(R.string.success_delete))
        }
        btnSaveNote.setOnClickListener {
            val content = etContent.text.toString()
            val title = etTitle.text.toString()
            viewModel.note.value.data?.let { note ->
                val updatedNote = note.copy(title = title, content = content)
                viewModel.onEvent(NoteDetailedEvent.UpdateNote(updatedNote))
            }
        }
    }


    override fun observeState() {
        lifecycleScope.launch {
            viewModel.note.collectAsUiState(
                context,
                viewLifecycleOwner,
                onSuccess = ::onSuccess,
                onError = ::onError,
                onLoading = stateLoadingBinding::loadingStarted
            )
        }
        lifecycleScope.launch {
            viewModel.isNoteSavedSuccessfully.observeWithLifecycle(viewLifecycleOwner) { isSaved ->
                if (isSaved) binding.root.showSnackbar(getString(R.string.success_save))
            }
        }
    }

    private fun onError(handledError: HandledError) = when (val error = handledError.error) {
        is InvalidNoteException -> when (error.field) {
            Field.TITLE -> binding.etTitle.error = handledError.message
            Field.CONTENT -> binding.etContent.error = handledError.message
        }
        is NotUniqueFieldException -> binding.etTitle.error = handledError.message
        else -> stateLoadingBinding.onError(handledError.message) {
            viewModel.onEvent(NoteDetailedEvent.Reload)
        }
    }

    private fun onSuccess(note: Note) {
        stateLoadingBinding.loadingFinished()
        with(binding) {
            etTitle.hint = getString(R.string.hint_note_title)
            etContent.hint = getString(R.string.hint_note_content)
            note.run {
                etTitle.setText(title)
                etContent.setText(content)
                date?.let { tvDate.text = it.formatToNoteDate() }
                if (!viewModel.isNewNote){
                    categories.toChipGroup(
                        chipgroupCategories,
                        isCheckedStyleEnabled = false,
                        onAddCategoryClick = { navigateToChooseCategoryDialog(note.id) },
                        onCategoryChipClick = { navigateToChooseCategoryDialog(note.id) })
                }
            }
        }
        stateLoadingBinding.loadingFinished()
    }

}
