package com.jetsada.firebasemvvmapplication.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.jetsada.firebasemvvmapplication.data.model.Task
import com.jetsada.firebasemvvmapplication.data.model.User
import com.jetsada.firebasemvvmapplication.data.repository.task.TaskRepository
import com.jetsada.firebasemvvmapplication.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    val repository: TaskRepository
): ViewModel() {

    private val _addTask = MutableLiveData<UiState<Pair<Task, String>>>()
    val addTask: LiveData<UiState<Pair<Task, String>>>
        get() = _addTask

    private val _getTask = MutableLiveData<UiState<List<Task>>>()
    val getTask: LiveData<UiState<List<Task>>>
        get() = _getTask

    fun addTask(task: Task){
        _addTask.value = UiState.Loading
        repository.addTask(task) { _addTask.value = it }
    }

    fun getTask(user: User?) {
        _getTask.value = UiState.Loading
        repository.getTask(user) { _getTask.value = it }
    }

}