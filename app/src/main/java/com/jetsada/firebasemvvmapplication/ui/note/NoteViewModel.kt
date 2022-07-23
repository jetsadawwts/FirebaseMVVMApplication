package com.jetsada.firebasemvvmapplication.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jetsada.firebasemvvmapplication.data.repository.NoteRepository
import com.jetsada.firebasemvvmapplication.model.Note
import com.jetsada.firebasemvvmapplication.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository): ViewModel() {

//    private val _note = MutableLiveData<ViewState>()
//    val note: LiveData<ViewState> get() = _note

    private val _notes = MutableLiveData<UiState<List<Note>>>()
    val note: LiveData<UiState<List<Note>>>
        get() = _notes

    private val _addNote = MutableLiveData<UiState<Pair<Note,String>>>()
    val addNote: LiveData<UiState<Pair<Note,String>>>
        get() = _addNote

    private val _updateNote = MutableLiveData<UiState<String>>()
    val updateNote: LiveData<UiState<String>>
        get() = _updateNote

    private val _deleteNote = MutableLiveData<UiState<String>>()
    val deleteNote: LiveData<UiState<String>>
        get() = _deleteNote

    fun getNotes() {
        _notes.value = UiState.Loading
        repository.getNotes { _notes.value = it }
    }

    fun addNote(note: Note){
        _addNote.value = UiState.Loading
        repository.addNote(note) { _addNote.value = it }
    }

    fun updateNote(note: Note){
        _updateNote.value = UiState.Loading
        repository.updateNote(note) { _updateNote.value = it }
    }

    fun deleteNote(note: Note){
        _deleteNote.value = UiState.Loading
        repository.deleteNote(note) { _deleteNote.value = it }
    }

//    fun getNotes() {
//            _note.value = ViewState(isLoading = true)
//            repository.getNotes {
//                when (it) {
//
//                    is UiState.Loading -> {
//                        _note.value = ViewState(isLoading = true)
//                    }
//
//                    is UiState.Success -> {
//                        _note.value = ViewState(NoteList = it.data)
//                    }
//                    is UiState.Failure -> {
//                        _note.value = ViewState(error = it.error.toString() ?: "An Unexpected Error")
//                    }
//                }
//            }
//    }
//
//
//    data class ViewState (
//        val isLoading : Boolean = false,
//        val NoteList : List<Note> = emptyList(),
//        val error : String = ""
//   )

//    fun getNotes() {
//        _note.value = UiState.Loading
//        viewModelScope.launch(Dispatchers.Main) {
//            delay(2000)
//            _note.value = repository.getNotes()
//        }
//    }

}