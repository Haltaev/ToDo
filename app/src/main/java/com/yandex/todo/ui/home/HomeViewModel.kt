package com.yandex.todo.ui.home

import androidx.lifecycle.ViewModel
import com.yandex.todo.data.api.ApiService
import com.yandex.todo.data.db.dao.TaskDao
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var taskDao: TaskDao

}