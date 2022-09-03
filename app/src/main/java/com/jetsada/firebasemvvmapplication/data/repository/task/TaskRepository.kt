package com.jetsada.firebasemvvmapplication.data.repository.task

import com.jetsada.firebasemvvmapplication.data.model.Task
import com.jetsada.firebasemvvmapplication.util.UiState

interface TaskRepository  {
    fun addTask(task: Task, result: (UiState<Pair<Task, String>>) -> Unit)
}