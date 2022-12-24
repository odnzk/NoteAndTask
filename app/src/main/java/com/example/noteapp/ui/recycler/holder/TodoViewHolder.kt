package com.example.noteapp.ui.recycler.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Todo
import com.example.noteapp.databinding.ItemTodoBinding
import com.example.noteapp.ui.util.ext.convertTUiString

class TodoViewHolder(
    private val binding: ItemTodoBinding,
    private val onTodoClick: ((Long) -> Unit)? = null,
    private val onCheckboxClick: ((Long, Boolean) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todo: Todo) {
        with(binding) {
            todo.run {
                tvTitle.text = title
                tvDeadlineDate.text = deadlineDate?.convertTUiString()
                ivDeadlineDateIcon.isVisible = deadlineDate != null
                rbCompleted.isChecked = isCompleted

//                selectedNote.categoriesToFlowCategories(flowCategories){
//                    // todo on category click
//                }
            }
            rbCompleted.setOnClickListener {
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
