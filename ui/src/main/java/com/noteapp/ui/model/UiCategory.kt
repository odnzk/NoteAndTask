package com.noteapp.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// helper ui model
@Parcelize
data class UiCategory(
    val id: Long,
    val color: Int,
    val title: String,
    val isSelected: Boolean = false
) : Parcelable
