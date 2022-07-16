package com.jetsada.firebasemvvmapplication.data.repository

import com.jetsada.firebasemvvmapplication.model.Note
import com.jetsada.firebasemvvmapplication.util.UiState

interface NoteRepository {
//    fun getNotes(): UiState<List<Note>>
    fun getNotes(result: (UiState<List<Note>>) -> Unit)
    fun addNotes(note: Note, result: (UiState<String>) -> Unit)
    fun updateNotes(note: Note, result: (UiState<String>) -> Unit)
}