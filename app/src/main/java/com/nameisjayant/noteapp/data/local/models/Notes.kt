package com.nameisjayant.noteapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Notes(
    val title:String,
    val description:String
){
    @PrimaryKey(autoGenerate = true)
    var noteId:Int? = null
}
