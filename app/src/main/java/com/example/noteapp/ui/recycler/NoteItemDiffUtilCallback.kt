package com.example.noteapp.ui.recycler

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.domain.model.Note
import com.example.domain.model.NoteItem
import com.example.domain.model.Todo

class NoteItemDiffUtilCallback : ItemCallback<NoteItem>() {
    override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem.id == newItem.id && oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return if (oldItem is Note) {
            oldItem == newItem as? Note
        } else {
            oldItem as? Todo == newItem as? Todo
        }
    }
}
