package com.jetsada.firebasemvvmapplication.data.repository.task

import com.google.firebase.database.FirebaseDatabase
import com.jetsada.firebasemvvmapplication.data.model.Task
import com.jetsada.firebasemvvmapplication.data.model.User
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.TASK
import com.jetsada.firebasemvvmapplication.util.UiState
import javax.inject.Inject

class TaskImplRepository@Inject constructor(val firebaseRealTime: FirebaseDatabase): TaskRepository {

    override fun addTask(task: Task, result: (UiState<Pair<Task, String>>) -> Unit) {
        val reference = firebaseRealTime.reference.child(TASK).push()
        val uniqueKey = reference.key ?: "invalid"
        task.id = uniqueKey
        reference
            .setValue(task)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(Pair(task,"Task has been created successfully"))
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

    override fun getTask(user: User?, result: (UiState<List<Task>>) -> Unit) {
        val reference = firebaseRealTime.reference.child(TASK).orderByChild("user_id").equalTo(user?.id)
        reference.get()
            .addOnSuccessListener {
                val tasks = arrayListOf<Task?>()
               for (item in it.children) {
                    val task = item.getValue(Task::class.java)
                    tasks.add(task)
               }
               result.invoke(
                   UiState.Success(
                       tasks.filterNotNull()
                   )
               )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }
}