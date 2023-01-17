package com.example.noteapp.ui.recycler.note

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Note

class NoteAdapter : ListAdapter<Note, NoteViewHolder>(NoteCallback()) {
    var onNoteClick: ((Long) -> Unit)? = null // (noteId: Long) -> Unit

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder.create(parent, onNoteClick)

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
        holder.bind(getItem(position))
}
