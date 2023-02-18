package com.nameisjayant.noteapp.data.local.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "note")
data class Notes(
    val title: String,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    var noteId: Int? = null
) : Parcelable
