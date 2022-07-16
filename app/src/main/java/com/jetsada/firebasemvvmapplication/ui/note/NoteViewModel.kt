package com.jetsada.firebasemvvmapplication.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetsada.firebasemvvmapplication.data.repository.NoteRepository
import com.jetsada.firebasemvvmapplication.model.Note
import com.jetsada.firebasemvvmapplication.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository): ViewModel() {

    private val _note = MutableLiveData <UiState<List<Note>>>()
    val note: LiveData<UiState<List<Note>>> get() = _note

    private val _addNote = MutableLiveData<UiState<String>>()
    val addNote: LiveData<UiState<String>> get() = _addNote

    private val _updateNote = MutableLiveData<UiState<String>>()
    val updateNote: LiveData<UiState<String>> get() = _updateNote

    fun addNote(note: Note) {
        _addNote.value = UiState.Loading
            repository.addNotes(note) {
                _addNote.value = it
            }
    }

    fun updateNote(note: Note) {
        _updateNote.value = UiState.Loading
        repository.updateNotes(note) {
            _updateNote.value = it
        }
    }

    fun getNotes() {
        _note.value = UiState.Loading
            repository.getNotes {
                _note.value = it
            }
    }

//    fun getNotes() {
//        _note.value = UiState.Loading
//        viewModelScope.launch(Dispatchers.Main) {
//            delay(2000)
//            _note.value = repository.getNotes()
//        }
//    }

}