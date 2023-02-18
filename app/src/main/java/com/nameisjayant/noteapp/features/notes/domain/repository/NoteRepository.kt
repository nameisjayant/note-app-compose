package com.nameisjayant.noteapp.features.notes.domain.repository

import com.nameisjayant.noteapp.data.local.models.Notes
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun addNote(notes: Notes)

    fun getAllNotes():Flow<List<Notes>>

    suspend fun deleteNote(note:Notes)

    suspend fun updateNote(note: Notes)

}