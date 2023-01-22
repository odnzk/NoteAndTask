package com.example.noteapp.ui.fragments.note.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.Note
import com.example.domain.util.exceptions.Field
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentDetailedNoteBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.util.CategoryOwnerType
import com.example.noteapp.ui.util.exceptions.InvalidNoteException
import com.example.noteapp.ui.util.ext.*
import com.example.noteapp.ui.util.handleState
import com.google.android.material.chip.Chip
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
        with(binding) {
            observeState()

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

    private fun observeState() =
        lifecycleScope.launch {
            viewModel.note.collectLatest { state ->
                state.handleState(
                    onLoadingAction = stateLoadingBinding::loadingStarted,
                    onSuccessAction = ::showNote,
                    onErrorAction = ::onErrorAction
                )
            }
        }

    private fun onErrorAction(error: Throwable) = when (error) {
        is InvalidNoteException -> {
            when (error.field) {
                Field.TITLE -> binding.etTitle.error = getString(R.string.error_invalid_note_tile)
                else -> binding.etContent.error = getString(R.string.error_invalid_note_content)
            }
        }
        else -> {
            stateLoadingBinding.errorOccurred(error) {
                viewModel.onEvent(NoteDetailedEvent.TryLoadingNoteAgain)
            }
        }
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
                categories.categoriesToFlowCategories(constraintLayout, flowCategories) {
                    val action =
                        NoteDetailedFragmentDirections.actionNoteDetailFragmentToChooseCategoryDialog(
                            type = CategoryOwnerType.NOTE_TYPE, noteId = note.id
                        )
                    findNavController().navigate(action)
                }

                Chip(context).setBtnAddCategoryStyle {
                    findNavController().navigate(
                        NoteDetailedFragmentDirections.actionNoteDetailFragmentToChooseCategoryDialog(
                            type = CategoryOwnerType.NOTE_TYPE, noteId = note.id
                        )
                    )
                }.also{ chip ->
                    chip.id = View.generateViewId()
                    constraintLayout.addView(chip)
                    flowCategories.addView(chip)
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
