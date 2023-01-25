package com.example.mainlist.api.noteitem

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.noteapp.model.Note
import com.noteapp.model.NoteItem
import com.noteapp.model.Todo

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
