package com.yandex.todo.data.api.repository

import androidx.lifecycle.LiveData
import com.yandex.todo.data.model.task.BaseResult
import com.yandex.todo.data.model.task.Task

interface TasksRepository {

    suspend fun downloadTasks(): BaseResult<Boolean>

    suspend fun getAllTasks(): LiveData<List<Task>>

    suspend fun getActiveTasks(): LiveData<List<Task>>

    suspend fun getActiveTasksForToday(): Int

    suspend fun updateRecentChangesStatus()

    suspend fun getTask(taskId: String): Task?

    suspend fun saveTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(taskId: String)

    suspend fun markAsDeleted(taskId: String)

    suspend fun unMarkAsDeleted(taskId: String)

    suspend fun removeDeletedTask()
}
