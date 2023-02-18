package com.nameisjayant.noteapp.features.notes.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nameisjayant.noteapp.common.base.NoteState
import com.nameisjayant.noteapp.data.local.models.Notes
import com.nameisjayant.noteapp.features.notes.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes: MutableState<NoteState<List<Notes>>> = mutableStateOf(NoteState.Loading)
    val notes: State<NoteState<List<Notes>>> = _notes

    private val _noteAddedEventFlow: MutableSharedFlow<NoteState<String>> = MutableSharedFlow()
    val noteAddedEventFlow = _noteAddedEventFlow.asSharedFlow()

    private val _deleteNoteEventFlow: MutableSharedFlow<NoteState<String>> = MutableSharedFlow()
    val deleteNoteEventFlow = _deleteNoteEventFlow.asSharedFlow()

    private val _updateNoteEventFlow: MutableSharedFlow<NoteState<String>> = MutableSharedFlow()
    val updateNoteEventFlow = _updateNoteEventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.getAllNotes()
                .onStart {
                    _notes.value = NoteState.Loading
                }.catch {
                    _notes.value = NoteState.Failure(it.message ?: "Something went wrong!")
                }.collect {
                    _notes.value = NoteState.Success(it)
                }
        }
    }

    fun onEvent(events: NoteEvents) {
        when (events) {
            is NoteEvents.NoteAddEvent -> {
                viewModelScope.launch {
                    _noteAddedEventFlow.emit(NoteState.Loading)
                    try {
                        repository.addNote(events.notes)
                        _noteAddedEventFlow.emit(NoteState.Success("Note Added"))
                    } catch (e: Exception) {
                        _noteAddedEventFlow.emit(
                            NoteState.Failure(
                                e.message ?: "Something went wrong!"
                            )
                        )
                    }

                }
            }
            is NoteEvents.DeleteNoteEvent -> {
                viewModelScope.launch {
                    _deleteNoteEventFlow.emit(NoteState.Loading)
                    try {
                        repository.deleteNote(events.note)
                        _deleteNoteEventFlow.emit(NoteState.Success("Note Deleted!"))
                    } catch (e: Exception) {
                        _deleteNoteEventFlow.emit(
                            NoteState.Failure(
                                e.message ?: "Something went wrong!!"
                            )
                        )
                    }
                }
            }
            is NoteEvents.UpdateNoteEvent -> {
                viewModelScope.launch {
                    _updateNoteEventFlow.emit(NoteState.Loading)
                    try {
                        repository.updateNote(events.note)
                        _updateNoteEventFlow.emit(NoteState.Success("Note Updated!"))
                    } catch (e: Exception) {
                        _updateNoteEventFlow.emit(
                            NoteState.Failure(
                                e.message ?: "Something went wrong!!"
                            )
                        )
                    }
                }
            }
        }
    }


}

sealed class NoteEvents {

    data class NoteAddEvent(val notes: Notes) : NoteEvents()
    data class DeleteNoteEvent(val note: Notes) : NoteEvents()

    data class UpdateNoteEvent(val note: Notes) : NoteEvents()

}