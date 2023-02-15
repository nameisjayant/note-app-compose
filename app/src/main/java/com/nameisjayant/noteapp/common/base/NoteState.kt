package com.nameisjayant.noteapp.common.base

sealed class NoteState<out T> {

    data class Success<T>(val data: T) : NoteState<T>()
    data class Failure(val msg: String) : NoteState<Nothing>()
    object Loading : NoteState<Nothing>()

}
