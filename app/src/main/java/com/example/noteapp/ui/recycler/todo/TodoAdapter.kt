package com.example.noteapp.ui.recycler.todo

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Todo

class TodoAdapter : ListAdapter<Todo, TodoViewHolder>(TodoCallback()) {
    var onTodoClick: ((Long) -> Unit)? = null
    var onCheckboxClick: ((Long, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder =
        TodoViewHolder.create(parent, onTodoClick, onCheckboxClick)

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) =
        holder.bind(getItem(position))
}
