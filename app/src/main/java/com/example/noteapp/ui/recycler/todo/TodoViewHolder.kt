package com.example.noteapp.ui.recycler.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Todo
import com.example.noteapp.databinding.ItemTodoBinding
import com.example.noteapp.ui.util.ext.categoriesToFlowCategories
import com.example.noteapp.ui.util.ext.formatToTodoDate

class TodoViewHolder(
    private val binding: ItemTodoBinding,
    private val onTodoClick: ((Long) -> Unit)? = null,
    private val onCheckboxClick: ((Long, Boolean) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todo: Todo) {
        with(binding) {
            todo.run {
                tvTitle.text = title
                tvDeadlineDate.text = deadlineDate?.formatToTodoDate()
                ivDeadlineDateIcon.isVisible = deadlineDate != null
                cbCompleted.isChecked = isCompleted

                categories.categoriesToFlowCategories(constraintLayout, flowCategories)
            }
            cbCompleted.setOnClickListener {
                onCheckboxClick?.invoke(todo.id, !todo.isCompleted)
            }
            root.setOnClickListener {
                onTodoClick?.invoke(todo.id)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTodoClick: ((Long) -> Unit)? = null,
            onCheckboxClick: ((Long, Boolean) -> Unit)? = null
        ): TodoViewHolder =
            TodoViewHolder(
                ItemTodoBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false),
                onTodoClick,
                onCheckboxClick
            )
    }
}
