package com.noteapp.feature_detailedscreens.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.domain.model.Note
import com.example.domain.validation.Field
import com.example.noteapp.ui.util.exceptions.InvalidNoteException
import com.google.android.material.chip.Chip
import com.noteapp.core.state.handleState
import com.noteapp.feature_detailedscreens.databinding.FragmentDetailedNoteBinding
import com.noteapp.feature_detailedscreens.internal.fragments.note.detailed.NoteDetailedEvent
import com.noteapp.feature_detailedscreens.internal.fragments.note.detailed.NoteDetailsViewModel
import com.noteapp.feature_detailedscreens.internal.navigation.fromNoteToChooseCategoryDialog
import com.noteapp.ui.R
import com.noteapp.ui.databinding.StateLoadingBinding
import com.noteapp.ui.ext.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
        initClickListeners()
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

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.note.collectLatest { state ->
                state.handleState(
                    onLoadingAction = stateLoadingBinding::loadingStarted,
                    onSuccessAction = ::showNote,
                    onErrorAction = ::onErrorAction
                )
            }
        }
    }

    private fun onErrorAction(error: Throwable) =
        if (error is InvalidNoteException) {
            when (error.field) {
                Field.TITLE -> binding.etTitle.error = getString(R.string.error_invalid_note_tile)
                else -> binding.etContent.error = getString(R.string.error_invalid_note_content)
            }
        } else stateLoadingBinding.errorOccurred(error) {
            viewModel.onEvent(NoteDetailedEvent.TryLoadingNoteAgain)
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
                categories.initCategoriesChipGroup(chipgroupCategories) {
                    findNavController().fromNoteToChooseCategoryDialog(note.id)
                }

                Chip(context).setBtnAddCategoryStyle {
                    findNavController().fromNoteToChooseCategoryDialog(note.id)
                }.also { chip ->
                    chipgroupCategories.addView(chip)
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
