package com.example.noteapp.ui.recycler.todo

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.domain.model.Todo

class TodoCallback : ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem == newItem
}
