package com.jetsada.firebasemvvmapplication.data.repository.note

import android.net.Uri
import com.jetsada.firebasemvvmapplication.data.model.Note
import com.jetsada.firebasemvvmapplication.data.model.User
import com.jetsada.firebasemvvmapplication.util.UiState

interface NoteRepository {
//    fun getNotes(): UiState<List<Note>>
    fun getNotes(user: User?,result: (UiState<List<Note>>) -> Unit)
    fun addNote(note: Note, result: (UiState<Pair<Note,String>>) -> Unit)
    fun updateNote(note: Note, result: (UiState<String>) -> Unit)
    fun deleteNote(note: Note, result: (UiState<String>) -> Unit)
    suspend fun uploadSingleFile(fileUri: Uri, result: (UiState<Uri>) -> Unit)
    suspend fun updateMultiFile(fileUri: List<Uri>, result: (UiState<List<Uri>>) -> Unit)
}