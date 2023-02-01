package com.noteapp.ui.recycler.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Note
import com.noteapp.ui.databinding.ItemNoteBinding
import com.noteapp.ui.ext.formatToNoteDate
import com.noteapp.ui.ext.toChipGroup

class NoteViewHolder(
    private val binding: ItemNoteBinding,
    private val onNoteClick: ((Long) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note) {
        with(binding) {
            note.run {
                tvTitle.text = title
                tvContent.text = content
                date?.let { tvDate.text = it.formatToNoteDate() }
                categories.toChipGroup(chipgroupCategories, isCheckedStyleEnabled = false)
            }
            root.setOnClickListener {
                onNoteClick?.invoke(note.id)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, onNoteClick: ((Long) -> Unit)?): NoteViewHolder =
            NoteViewHolder(
                ItemNoteBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false),
                onNoteClick
            )
    }
}
