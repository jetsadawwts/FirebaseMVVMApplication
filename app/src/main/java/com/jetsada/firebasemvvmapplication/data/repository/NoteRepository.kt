package com.jetsada.firebasemvvmapplication.data.repository

import com.jetsada.firebasemvvmapplication.model.Note
import com.jetsada.firebasemvvmapplication.util.UiState

interface NoteRepository {
//    fun getNotes(): UiState<List<Note>>
    fun getNotes(result: (UiState<List<Note>>) -> Unit)
    fun addNote(note: Note, result: (UiState<Pair<Note,String>>) -> Unit)
    fun updateNote(note: Note, result: (UiState<String>) -> Unit)
    fun deleteNote(note: Note, result: (UiState<String>) -> Unit)
}