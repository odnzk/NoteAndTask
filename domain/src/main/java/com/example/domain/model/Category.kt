package com.example.domain.model

import androidx.annotation.ColorInt

data class Category(
    val id: Long = 0,
    val title: String,
    @ColorInt val color: Int
)
