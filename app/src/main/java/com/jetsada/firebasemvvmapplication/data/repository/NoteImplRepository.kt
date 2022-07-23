package com.jetsada.firebasemvvmapplication.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jetsada.firebasemvvmapplication.model.Note
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.DATE
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.NOTE
import com.jetsada.firebasemvvmapplication.util.UiState
import javax.inject.Inject

class NoteImplRepository @Inject constructor(val fireStorage: FirebaseFirestore): NoteRepository {

    override fun getNotes(result: (UiState<List<Note>>) -> Unit) {
        fireStorage.collection(NOTE)
            .orderBy(DATE, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<Note>()
                for (document in it) {
                    val note = document.toObject(Note::class.java)
                    notes.add(note)
                }
                result.invoke(
                    UiState.Success(notes)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun addNote(note: Note, result: (UiState<Pair<Note,String>>) -> Unit) {
        val document = fireStorage.collection(NOTE).document()
        note.id = document.id
        document
            .set(note)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(Pair(note,"Note has been created successfully"))
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun updateNote(note: Note, result: (UiState<String>) -> Unit) {
        val document = fireStorage.collection(NOTE).document(note.id)
        document
            .set(note)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Note has been update successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun deleteNote(note: Note, result: (UiState<String>) -> Unit) {
        fireStorage.collection(NOTE).document(note.id)
            .delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note successfully deleted!"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure(e.message))
            }
    }

//    override fun getNotes(): UiState<List<Note>> {
//        val data = listOf<Note>()
//
//        if(data.isNullOrEmpty()) {
//            return UiState.Failure("Data is Empty")
//        } else {
//            return UiState.Success(data)
//        }
//
//    }


//    override fun getNotes(): List<Note> {
//        //We will get data from firebase
//        return arrayListOf<Note>(
//            Note(
//                id = "1",
//                text = "Note1",
//                date = Date()
//            ),
//            Note(
//                id = "2",
//                text = "Note2",
//                date = Date()
//            ),
//            Note(
//                id = "3",
//                text = "Note3",
//                date = Date()
//            )
//        )
//    }

}