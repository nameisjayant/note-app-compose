package com.nameisjayant.noteapp.data.local.models

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "note")
data class Notes(
    val title: String,
    val description: String,
    val category: String,
    val color: Int,
    @PrimaryKey(autoGenerate = true)
    var noteId: Int? = null
) : Parcelable
