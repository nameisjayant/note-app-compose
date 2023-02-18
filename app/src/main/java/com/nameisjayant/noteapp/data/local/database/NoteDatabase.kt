package com.nameisjayant.noteapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nameisjayant.noteapp.data.local.dao.NoteDao
import com.nameisjayant.noteapp.data.local.models.Notes

@Database(version = 4, entities = [Notes::class], exportSchema = false)
abstract class NoteDatabase : RoomDatabase(){

    abstract val noteDao: NoteDao

}