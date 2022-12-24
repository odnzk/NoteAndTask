package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.noteapp.databinding.FragmentDetailedNoteBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.ext.categoriesToFlowCategories
import com.example.noteapp.ui.util.ext.toChip
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
import com.example.noteapp.ui.viewmodel.MainViewModel
import com.example.noteapp.ui.viewmodel.NoteItemEvent
import com.example.noteapp.ui.viewmodel.handleState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {
    private var _binding: FragmentDetailedNoteBinding? = null
    private val binding get() = _binding!!

    private val stateLoadingBinding by lazy {
        StateLoadingBinding.bind(binding.root)
    }

    private val viewModel: MainViewModel by viewModels()
    private val noteDetailFragmentArgs: NoteDetailFragmentArgs by navArgs()
    private val selectedNoteId: Long by lazy { noteDetailFragmentArgs.noteId }
    private var selectedNote: Note = Note.defaultInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            observeState()

            btnDelete.setOnClickListener {
                viewModel.onEvent(NoteItemEvent.DeleteItem(selectedNote))
            }

            etContent.doAfterTextChanged {
                selectedNote = selectedNote.copy(content = etContent.text.toString())
                viewModel.onEvent(NoteItemEvent.UpdateNoteItem(selectedNote))
            }

            etTitle.doAfterTextChanged {
                selectedNote = selectedNote.copy(title = etTitle.text.toString())
                viewModel.onEvent(NoteItemEvent.UpdateNoteItem(selectedNote))
            }

        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenCreated {
            viewModel.noteItemsListState.collectLatest { listState ->
                listState.handleState(
                    onErrorAction = ::onErrorAction,
                    onLoadingAction = { stateLoadingBinding.loadingStarted() },
                    onSuccessAction = ::onSuccessAction
                )
            }
        }
    }

    private fun onErrorAction(error: Throwable) {
        stateLoadingBinding.errorOccurred(error) {
            viewModel.loadData()
        }
    }

    private fun onSuccessAction(data: List<NoteItem>) {
        selectedNote = data.first { it.id == selectedNoteId } as Note
        with(binding) {
            etTitle.setText(selectedNote.title)
            etContent.setText(selectedNote.content)
            tvDate.text = selectedNote.date.toString()

            selectedNote.categoriesToFlowCategories(flowCategories){
                // todo on category click
            }
//            selectedNote.categories.takeIf { it.isNotEmpty() }?.let {
//                it.forEach { category ->
//                    flowCategories.addView(category.toChip(requireContext()) {
//
//                    })
//                }
//            }
        }
        stateLoadingBinding.loadingFinished()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
