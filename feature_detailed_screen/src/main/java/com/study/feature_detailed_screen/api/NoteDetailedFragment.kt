package com.study.feature_detailed_screen.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.Note
import com.noteapp.ui.R
import com.noteapp.ui.collectAsUiState
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.*
import com.noteapp.ui.observeWithLifecycle
import com.study.feature_detailed_screen.databinding.FragmentDetailedNoteBinding
import com.study.feature_detailed_screen.internal.fragments.note.detailed.NoteDetailedEvent
import com.study.feature_detailed_screen.internal.fragments.note.detailed.NoteDetailsViewModel
import com.study.feature_detailed_screen.internal.navigation.fromNoteToChooseCategoryDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteDetailedFragment : Fragment() {
    private var _binding: FragmentDetailedNoteBinding? = null
    private val binding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val viewModel: NoteDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        observeIsNoteSaved()
        initClickListeners()
    }

    private fun observeIsNoteSaved() =
        lifecycleScope.launch {
            viewModel.isNoteSavedSuccessfully.observeWithLifecycle(viewLifecycleOwner) { isSaved ->
                if (isSaved) {
                    binding.root.showSnackbar(getString(R.string.success_save))
                }

            }
        }


    private fun initClickListeners() {
        with(binding) {
            btnDelete.setOnClickListener {
                viewModel.onEvent(NoteDetailedEvent.DeleteNote)
                root.showSnackbar(getString(R.string.success_delete))
            }

            btnSaveNote.setOnClickListener {
                val content = etContent.text.toString()
                val title = etTitle.text.toString()
                viewModel.note.value.data?.let { note ->
                    viewModel.onEvent(
                        NoteDetailedEvent.UpdateNote(
                            note.copy(
                                title = title, content = content
                            )
                        )
                    )
                }
            }
        }
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.note.collectAsUiState(
            context,
            viewLifecycleOwner,
            onSuccess = ::showNote,
            onError = this@NoteDetailedFragment::onError,
            onLoading = stateLoadingBinding::loadingStarted
        )
    }

    private fun onError(handledError: HandledError) =
        stateLoadingBinding.onError(handledError.message) {
            viewModel.onEvent(NoteDetailedEvent.Reload)
        }


    private fun showNote(note: Note) {
        stateLoadingBinding.loadingFinished()
        with(binding) {
            etTitle.hint = getString(R.string.hint_note_title)
            etContent.hint = getString(R.string.hint_note_content)
            note.run {
                etTitle.setText(title)
                etContent.setText(content)
                date?.let {
                    tvDate.text = it.formatToNoteDate()
                }
                if (!viewModel.isNewNote){
                    categories.toChipGroup(
                        chipgroupCategories,
                        isCheckedStyleEnabled = false,
                        onAddCategoryClick = { findNavController().fromNoteToChooseCategoryDialog(note.id) },
                        onCategoryChipClick = { findNavController().fromNoteToChooseCategoryDialog(note.id) })
                }
            }
        }
        stateLoadingBinding.loadingFinished()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedNoteBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }
}
