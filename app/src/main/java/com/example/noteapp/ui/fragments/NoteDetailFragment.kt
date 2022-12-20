package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.domain.model.Note
import com.example.noteapp.databinding.FragmentDetailedNoteBinding
import com.example.noteapp.ui.viewmodel.MainViewModel
import com.example.noteapp.ui.viewmodel.NoteItemEvent

class NoteDetailFragment : Fragment() {
    private var _binding: FragmentDetailedNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

//    private val args by navArgs<List>()
//    private val userId by lazy { args.noteId }
//    // todo
private var displayedNote: Note =
    Note(id = 1, title = "", content = "", category = emptyList(), date = null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnDelete.setOnClickListener {
                viewModel.onEvent(NoteItemEvent.DeleteItem(displayedNote))
            }
            etContent.doAfterTextChanged {
                displayedNote = displayedNote.copy(content = etContent.text.toString())
                viewModel.onEvent(NoteItemEvent.UpdateNoteItem(displayedNote))
            }
            etTitle.doAfterTextChanged {
                displayedNote = displayedNote.copy(title = etTitle.text.toString())
                viewModel.onEvent(NoteItemEvent.UpdateNoteItem(displayedNote))
            }
            categoryLayout.category.setOnClickListener {
                // todo change category
                // 1) show dialog and get curr id to him
                // 2) viewModel.onEvent(NoteItemEvent.UpdateNoteItem(displayedNote)) from dialog
            }
        }
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
