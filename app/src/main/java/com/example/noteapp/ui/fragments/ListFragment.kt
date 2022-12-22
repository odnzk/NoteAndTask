package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.databinding.FragmentListBinding
import com.example.noteapp.ui.recycler.NoteItemAdapter
import com.example.noteapp.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val listAdapter = NoteItemAdapter()
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding) {
            recyclerView.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listAdapter

//            todo addItemDecoration() + itemAnimator
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        initAdapter()

        return binding.root
    }

    private fun initAdapter() {
        listAdapter.onNoteClick = ::onNoteClick
        listAdapter.onTodoClick = ::onTodoClick
        listAdapter.submitList(emptyList())
    }

    private fun onNoteClick(noteId: Long) {
        val action = ListFragmentDirections.actionListFragmentToNoteDetailFragment(noteId)
        findNavController().navigate(action)
    }

    private fun onTodoClick(todoId: Long) {
        val action = ListFragmentDirections.actionListFragmentToTodoDetailFragment(todoId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
