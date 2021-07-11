package com.yandex.todo.data.api.repository

import androidx.lifecycle.LiveData
import com.yandex.todo.data.api.model.task.BaseResult
import com.yandex.todo.data.api.model.task.Task

interface TasksRepository {

    suspend fun getTasks(forceUpdate: Boolean = false): BaseResult<List<Task>>

    suspend fun getTask(taskId: String, forceUpdate: Boolean = false): BaseResult<Task>

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}
