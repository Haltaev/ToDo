package com.yandex.todo.data.api.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.yandex.todo.data.api.ApiService
import com.yandex.todo.data.db.TasksDatabase
import com.yandex.todo.data.model.requests.RequestForSync
import com.yandex.todo.data.model.task.BaseResult
import com.yandex.todo.data.model.task.Task
import com.yandex.todo.data.model.task.TaskDaoEntity
import com.yandex.todo.data.model.task.TaskNetworkEntity
import com.yandex.todo.data.model.task.mapper.mapFromDaoEntity
import com.yandex.todo.data.model.task.mapper.mapFromListOfDaoEntity
import com.yandex.todo.data.model.task.mapper.mapToDaoEntity
import com.yandex.todo.data.model.task.mapper.mapToNetworkEntity
import kotlinx.coroutines.*
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val api: ApiService,
    tasksDatabase: TasksDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TasksRepository {

    private val tasksDao = tasksDatabase.taskDao()

    override suspend fun getAllTasks(): LiveData<List<Task>> {
        return withContext(ioDispatcher) {
            Transformations.map(tasksDao.observeTasks()) {
                mapFromListOfDaoEntity(it)
            }
        }
    }

    override suspend fun getActiveTasks(): LiveData<List<Task>> {

        return withContext(ioDispatcher) {
            Transformations.map(tasksDao.observeActiveTasks()) {
                mapFromListOfDaoEntity(it)
            }
        }
    }

    override suspend fun getActiveTasksForToday(): Int {
        val currentDate = Date(System.currentTimeMillis())
        return withContext(ioDispatcher) {
            tasksDao.observeActiveTasksForToday(
                currentDate.roundStartDay(),
                currentDate.roundEndDay()
            ).size
        }
    }

    override suspend fun updateRecentChangesStatus() {
        return withContext(ioDispatcher) {
            tasksDao.updateRecentChangesStatus()
        }
    }

    override suspend fun getTask(taskId: String): Task? {
        return withContext(ioDispatcher) {
            tasksDao.getTaskById(taskId)?.mapFromDaoEntity()
        }
    }

    override suspend fun downloadTasks(): BaseResult<Boolean> {
        try {
            return withContext(ioDispatcher) {
                when (val requestBody = loadTasks()) {
                    is BaseResult.Error -> {
                        BaseResult.Error.OtherErrors
                    }
                    is BaseResult.Success -> {
                        if (requestBody.data.other.isEmpty() && requestBody.data.deleted.isEmpty()) {
                            BaseResult.Success(true)
                        } else {
                            val syncResponse = api.syncTasks(requestBody.data)
                            if (syncResponse.isSuccessful) {
                                requestBody.data.deleted.forEach { tasksDao.unMarkAsDirty(it) }
                                removeDeletedTask()
                                BaseResult.Success(true)
                            } else {
                                BaseResult.Error.OtherErrors
                            }
                        }
                    }
                }
            }
        } catch (e: UnknownHostException) {
            return BaseResult.Error.UnknownHost
        } catch (e: Exception) {
            return BaseResult.Error.OtherErrors
        }
    }

    private suspend fun loadTasks(): BaseResult<RequestForSync> {
        try {
            val networkResponse = api.getTasks()
            val localResponse = tasksDao.getTasks()
            if (networkResponse.isSuccessful) {
                val body = networkResponse.body()
                if (body != null) {
                    val requestBody = RequestForSync(arrayListOf(), arrayListOf())
                    val networkTasksMap = hashMapOf<String, TaskNetworkEntity>()
                    val localTasksMap = hashMapOf<String, TaskDaoEntity>()
                    body.forEach {
                        networkTasksMap[it.id] = it
                    }
                    localResponse.forEach { localTask ->
                        if (localTask.isDirty) {
                            if (localTask.isDeleted)
                                networkTasksMap[localTask.id]?.id?.let {
                                    requestBody.deleted.add(it)
                                }
                            else {
                                networkTasksMap[localTask.id]?.let { task ->
                                    requestBody.other.add(task)
                                }
                            }
                        }
                        localTasksMap[localTask.id] = localTask
                    }
                    body.forEach { bodyItem ->
                        val item = localTasksMap[bodyItem.id]
                        if (item == null) {
                            tasksDao.insertTask(
                                bodyItem.mapToDaoEntity().copy(isDirty = false)
                            )
                        } else {
                            tasksDao.insertTask(
                                item.copy(isDirty = false)
                            )
                        }
                    }
                    return BaseResult.Success(requestBody)
                } else {
                    return BaseResult.Error.OtherErrors
                }
            } else {
                return BaseResult.Error.OtherErrors
            }
        } catch (e: UnknownHostException) {
            return BaseResult.Error.UnknownHost
        } catch (e: Exception) {
            return BaseResult.Error.OtherErrors
        }
    }

    override suspend fun saveTask(task: Task) {
        coroutineScope {
            launch { tasksDao.insertTask(task.mapToDaoEntity()) }
            launch {
                try {
                    val response = api.saveTask(task.mapToNetworkEntity())
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            tasksDao.unMarkAsDirty(task.id)
                        }
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    override suspend fun completeTask(taskId: String) {
        withContext(ioDispatcher) {
            tasksDao.completeTask(taskId)
        }
    }

    override suspend fun activateTask(taskId: String) {
        withContext(ioDispatcher) {
            tasksDao.activateTask(taskId)
        }
    }

    override suspend fun markAsDeleted(taskId: String) {
        withContext(ioDispatcher) {
            tasksDao.markAsDeleted(taskId)
        }
    }

    override suspend fun unMarkAsDeleted(taskId: String) {
        withContext(ioDispatcher) {
            tasksDao.unMarkAsDeleted(taskId)
        }
    }

    override suspend fun removeDeletedTask() {
        withContext(ioDispatcher) {
            tasksDao.removeDeletedTask()
        }
    }

    fun Date.roundStartDay(): Long {
        val date = Calendar.getInstance()
        date.time = this
        date.set(Calendar.AM_PM, Calendar.AM)
        date.set(Calendar.HOUR, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        return date.timeInMillis
    }

    fun Date.roundEndDay(): Long {
        val date = Calendar.getInstance()
        date.time = this
        date.set(Calendar.AM_PM, Calendar.AM)
        date.set(Calendar.HOUR, 23)
        date.set(Calendar.MINUTE, 59)
        date.set(Calendar.SECOND, 59)
        return date.timeInMillis
    }
}