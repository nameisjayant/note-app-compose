package com.nameisjayant.noteapp.data.local.models

import androidx.compose.ui.graphics.Color
import com.nameisjayant.noteapp.ui.theme.Teal200

data class Category(
    val id: Int,
    val title: String,
    val color: Color
)

val categoryList = listOf(
    Category(
        1,
        "Important",
        Color.Red
    ),
    Category(
        2,
        "To do",
        Teal200
    ),
    Category(
        3,
        "Idea",
        Color.Green
    )
)