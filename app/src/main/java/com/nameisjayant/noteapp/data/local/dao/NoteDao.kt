package com.nameisjayant.noteapp.data.local.dao

import androidx.room.*
import com.nameisjayant.noteapp.data.local.models.Notes
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(notes: Notes)

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Notes>>

    @Delete
    suspend fun deleteNote(note: Notes)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Notes)
}