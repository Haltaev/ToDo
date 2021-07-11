/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yandex.todo.data.api.repository

import com.yandex.todo.data.api.model.task.BaseResult
import com.yandex.todo.data.api.model.task.Task
import com.yandex.todo.data.db.datasource.TasksDataSource
import com.yandex.todo.di.module.AppModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 *
 * To simplify the sample, this repository only uses the local data source only if the remote
 * data source fails. Remote is the source of truth.
 */
class DefaultTasksRepository @Inject constructor(
    @AppModule.TasksLocalDataSource private val tasksLocalDataSource: TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksRepository {

    private var cachedTasks: ConcurrentMap<String, Task>? = null

    override suspend fun getTasks(forceUpdate: Boolean): BaseResult<List<Task>> {

            return withContext(ioDispatcher) {
                // Respond immediately with cache if available and not dirty
                if (!forceUpdate) {
                    cachedTasks?.let { cachedTasks ->
                        return@withContext BaseResult.Success(cachedTasks.values.sortedBy { it.id })
                    }
                }

                val newTasks = fetchTasksFromRemoteOrLocal(forceUpdate)

                // Refresh the cache with the new tasks
                (newTasks as? BaseResult.Success)?.let { refreshCache(it.data) }

                cachedTasks?.values?.let { tasks ->
                    return@withContext BaseResult.Success(tasks.sortedBy { it.id })
                }

                (newTasks as? BaseResult.Success)?.let {
                    if (it.data.isEmpty()) {
                        return@withContext BaseResult.Success(it.data)
                    }
                }
                return@withContext BaseResult.Error(Exception("Illegal state"))
            }
    }

    private suspend fun fetchTasksFromRemoteOrLocal(forceUpdate: Boolean): BaseResult<List<Task>> {
        // Remote first
//        val remoteTasks = tasksRemoteDataSource.getTasks()
//        when (remoteTasks) {
//            is Error -> {}
//            is BaseResult.Success -> {
//                refreshLocalDataSource(remoteTasks.data)
//                return remoteTasks
//            }
//            else -> throw IllegalStateException()
//        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return BaseResult.Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localTasks = tasksLocalDataSource.getTasks()
        if (localTasks is BaseResult.Success) return localTasks
        return BaseResult.Error(Exception("Error fetching from remote and local"))
    }

    /**
     * Relies on [getTasks] to fetch data and picks the task with the same ID.
     */
    override suspend fun getTask(taskId: String, forceUpdate: Boolean): BaseResult<Task> {

            return withContext(ioDispatcher) {
                // Respond immediately with cache if available
                if (!forceUpdate) {
                    getTaskWithId(taskId)?.let {
                        return@withContext BaseResult.Success(it)
                    }
                }

                val newTask = fetchTaskFromRemoteOrLocal(taskId, forceUpdate)

                // Refresh the cache with the new tasks
                (newTask as? BaseResult.Success)?.let { cacheTask(it.data) }

                return@withContext newTask
            }
    }

    private suspend fun fetchTaskFromRemoteOrLocal(
        taskId: String,
        forceUpdate: Boolean
    ): BaseResult<Task> {
        // Remote first
//        val remoteTask = tasksRemoteDataSource.getTask(taskId)
//        when (remoteTask) {
//            is Error -> {}
//            is BaseResult.Success -> {
//                refreshLocalDataSource(remoteTask.data)
//                return remoteTask
//            }
//            else -> throw IllegalStateException()
//        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return BaseResult.Error(Exception("Refresh failed"))
        }

        // Local if remote fails
        val localTasks = tasksLocalDataSource.getTask(taskId)
        if (localTasks is BaseResult.Success) return localTasks
        return BaseResult.Error(Exception("Error fetching from remote and local"))
    }

    override suspend fun saveTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            coroutineScope {
//                launch { tasksRemoteDataSource.saveTask(it) }
                launch { tasksLocalDataSource.saveTask(it) }
            }
        }
    }

    override suspend fun completeTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isDone = true
            coroutineScope {
//                launch { tasksRemoteDataSource.completeTask(it) }
                launch { tasksLocalDataSource.completeTask(it) }
            }
        }
    }

    override suspend fun completeTask(taskId: String) {
        withContext(ioDispatcher) {
            getTaskWithId(taskId)?.let {
                completeTask(it)
            }
        }
    }

    override suspend fun activateTask(task: Task) = withContext(ioDispatcher) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isDone = false
            coroutineScope {
//                launch { tasksRemoteDataSource.activateTask(it) }
                launch { tasksLocalDataSource.activateTask(it) }
            }
        }
    }

    override suspend fun activateTask(taskId: String) {
        withContext(ioDispatcher) {
            getTaskWithId(taskId)?.let {
                activateTask(it)
            }
        }
    }

    override suspend fun clearCompletedTasks() {
        coroutineScope {
//            launch { tasksRemoteDataSource.clearCompletedTasks() }
            launch { tasksLocalDataSource.clearCompletedTasks() }
        }
        withContext(ioDispatcher) {
            cachedTasks?.entries?.removeAll { it.value.isDone }
        }
    }

    override suspend fun deleteAllTasks() {
        withContext(ioDispatcher) {
            coroutineScope {
//                launch { tasksRemoteDataSource.deleteAllTasks() }
                launch { tasksLocalDataSource.deleteAllTasks() }
            }
        }
        cachedTasks?.clear()
    }

    override suspend fun deleteTask(taskId: String) {
        coroutineScope {
//            launch { tasksRemoteDataSource.deleteTask(taskId) }
            launch { tasksLocalDataSource.deleteTask(taskId) }
        }

        cachedTasks?.remove(taskId)
    }

    private fun refreshCache(tasks: List<Task>) {
        cachedTasks?.clear()
        tasks.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(tasks: List<Task>) {
        tasksLocalDataSource.deleteAllTasks()
        for (task in tasks) {
            tasksLocalDataSource.saveTask(task)
        }
    }

    private suspend fun refreshLocalDataSource(task: Task) {
        tasksLocalDataSource.saveTask(task)
    }

    private fun getTaskWithId(id: String) = cachedTasks?.get(id)

    private fun cacheTask(task: Task): Task {
        if (cachedTasks == null) {
            cachedTasks = ConcurrentHashMap()
        }
        cachedTasks?.put(task.id, task)
        return task
    }

    private inline fun cacheAndPerform(task: Task, perform: (Task) -> Unit) {
        val cachedTask = cacheTask(task)
        perform(cachedTask)
    }
}
