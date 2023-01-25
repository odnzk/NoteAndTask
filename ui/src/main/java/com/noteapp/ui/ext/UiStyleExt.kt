package com.noteapp.ui.ext


import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

fun RecyclerView.initStandardVerticalRecyclerView(itemTouchHelper: ItemTouchHelper? = null) {
    this.apply {
        layoutManager = LinearLayoutManager(context)
        itemTouchHelper?.attachToRecyclerView(this)
        addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
}

fun Date.formatToTodoDate(pattern: String? = "EE, dd.MM"): String =
    SimpleDateFormat(pattern).format(this)


// todo move to strings.xml
fun Calendar.formatToReminderString(): String =
    SimpleDateFormat("dd.MM.yy HH:mm").format(this)

fun Date.formatToNoteDate(pattern: String? = "dd.MM.yy"): String =
    SimpleDateFormat(pattern).format(this)


