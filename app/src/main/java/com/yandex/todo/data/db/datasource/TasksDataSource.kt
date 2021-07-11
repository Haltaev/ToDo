package com.yandex.todo.data.db.datasource

import androidx.lifecycle.LiveData
import com.yandex.todo.data.api.model.task.BaseResult
import com.yandex.todo.data.api.model.task.Task


interface TasksDataSource {

    fun observeTasks(): LiveData<BaseResult<List<Task>>>

    suspend fun getTasks(): BaseResult<List<Task>>

    suspend fun refreshTasks()

    fun observeTask(taskId: String): LiveData<BaseResult<Task>>

    suspend fun getTask(taskId: String): BaseResult<Task>

    suspend fun refreshTask(taskId: String)

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}
