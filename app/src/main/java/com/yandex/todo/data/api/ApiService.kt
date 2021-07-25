package com.yandex.todo.data.api

import com.yandex.todo.data.model.requests.RequestForSync
import com.yandex.todo.data.model.task.TaskNetworkEntity
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("/tasks/")
    suspend fun getTasks(): Response<List<TaskNetworkEntity>>

    @POST("/tasks/")
    suspend fun saveTask(
        @Body task: TaskNetworkEntity
    ): Response<TaskNetworkEntity>

    @PUT("/tasks/")
    suspend fun syncTasks(
        @Body tasks: RequestForSync
    ): Response<Any>
}