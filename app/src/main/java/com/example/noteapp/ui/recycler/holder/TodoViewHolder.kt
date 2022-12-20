package com.example.noteapp.ui.recycler.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Todo
import com.example.noteapp.databinding.ItemTodoBinding

class TodoViewHolder(
    private val binding: ItemTodoBinding,
    private val onTodoClick: ((Long) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todo: Todo) {
        with(binding) {
            todo.run {
                tvTitle.text = title
                tvDeadlineDate.text = deadlineDate.toString()
//                category?.let {
//                    includedCategory.category.text = it.title
//                    includedCategory.category.setChipBackgroundColorResource(R.color.category_blue)
//                }
                rbCompleted.isChecked = isCompleted
            }
            root.setOnClickListener {
                onTodoClick?.invoke(todo.id)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, onTodoClick: ((Long) -> Unit)?): TodoViewHolder =
            TodoViewHolder(
                ItemTodoBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false), onTodoClick
            )
    }
}
