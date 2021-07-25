package com.yandex.todo.data.model.requests

import com.yandex.todo.data.model.task.TaskNetworkEntity

data class RequestForSync(
    val deleted: ArrayList<String>,
    val other: ArrayList<TaskNetworkEntity>,
)