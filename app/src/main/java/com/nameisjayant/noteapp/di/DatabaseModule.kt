package com.nameisjayant.noteapp.di

import android.app.Application
import androidx.room.Room
import com.nameisjayant.noteapp.data.local.dao.NoteDao
import com.nameisjayant.noteapp.data.local.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application): NoteDatabase =
        Room.databaseBuilder(application, NoteDatabase::class.java, "NoteDatabase")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesNoteDao(db: NoteDatabase): NoteDao = db.noteDao
}