package com.yandex.todo.data.api

import androidx.lifecycle.LiveData
import com.yandex.todo.data.api.model.task.BaseResult
import com.yandex.todo.data.api.model.task.Task
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/tasks/")
    suspend fun getTasks(): Response<BaseResult<List<Task>>>
}