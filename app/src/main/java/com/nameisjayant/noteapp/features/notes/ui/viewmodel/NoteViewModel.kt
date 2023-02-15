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
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes: MutableState<NoteState<List<Notes>>> = mutableStateOf(NoteState.Loading)
    val notes: State<NoteState<List<Notes>>> = _notes

    private val _noteAddedEventFlow: MutableSharedFlow<NoteState<String>> = MutableSharedFlow()
    val noteAddedEventFlow = _noteAddedEventFlow.asSharedFlow()

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
                    } catch (e: java.lang.Exception) {
                        _noteAddedEventFlow.emit(
                            NoteState.Failure(
                                e.message ?: "Something went wrong!"
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

}