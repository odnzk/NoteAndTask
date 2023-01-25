package com.noteapp.model

data class Category(
    val id: Long = 0,
    val title: String,
    val color: Int
) {
    companion object {
        private const val DEFAULT_BLUE_COLOR = -8286721
        fun defaultInstance() = Category(0, "", DEFAULT_BLUE_COLOR)
    }
}
