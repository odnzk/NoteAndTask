package com.noteapp.ui.recycler.note

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.domain.model.Note

class NoteCallback : ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = newItem.id == oldItem.id
    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
}
