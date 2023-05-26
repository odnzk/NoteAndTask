package com.example.mainlist.utils.recyler

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.domain.model.Todo
import com.noteapp.ui.R
import com.noteapp.ui.recycler.note.NoteViewHolder
import com.noteapp.ui.recycler.todo.TodoViewHolder

internal class NoteItemAdapter : ListAdapter<NoteItem, RecyclerView.ViewHolder>(NoteItemDiffUtilCallback()) {
    var onNoteClick: ((Long) -> Unit)? = null
    var onTodoClick: ((Long) -> Unit)? = null
    var onTodoCheckboxClick: ((Long, Boolean) -> Unit)? = null


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Note -> R.layout.item_note
            is Todo -> R.layout.item_todo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_note -> NoteViewHolder.create(
                parent,
                onNoteClick
            )
            else -> TodoViewHolder.create(parent, onTodoClick, onTodoCheckboxClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return when (val item = getItem(position)) {
            is Note -> (holder as NoteViewHolder).bind(item)
            is Todo -> (holder as TodoViewHolder).bind(item)
        }
    }
}
