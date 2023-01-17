package com.example.noteapp.ui.recycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SwipeCallback(
    private val adapter: ListAdapter<out Any, out ViewHolder>,
    private inline val onSwipeItem: (Any?) -> Unit
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        viewHolder
            .adapterPosition
            .takeIf { position -> position != RecyclerView.NO_POSITION }
            ?.let { position ->
                adapter.currentList[position]?.let { onSwipeItem(it) }
            }
    }
}
