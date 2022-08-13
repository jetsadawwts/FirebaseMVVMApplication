package com.jetsada.firebasemvvmapplication.data.repository.note

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.jetsada.firebasemvvmapplication.data.model.Note
import com.jetsada.firebasemvvmapplication.data.model.User
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.DATE
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.NOTE
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.USER_ID
import com.jetsada.firebasemvvmapplication.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteImplRepository @Inject constructor(val fireStorage: FirebaseFirestore, val storageReference: StorageReference): NoteRepository {

    override fun getNotes(user: User?, result: (UiState<List<Note>>) -> Unit) {
        fireStorage.collection(NOTE)
            .whereEqualTo(USER_ID, user?.id)
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

    override suspend fun uploadSingleFile(fileUri: Uri, result: (UiState<Uri>) -> Unit) {
        try {
            val uri: Uri = withContext(Dispatchers.IO) {
                storageReference
                    .putFile(fileUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }
            result.invoke(UiState.Success(uri))
        } catch (e: FirebaseException){
            result.invoke(UiState.Failure(e.message))
        }catch (e: Exception){
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