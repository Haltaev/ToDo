package com.yandex.todo.data.model.task

import com.google.gson.annotations.SerializedName

data class TaskNetworkEntity(
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("importance")
    val importance: String,
    @SerializedName("done")
    val isDone: Boolean,
    @SerializedName("deadline")
    val deadline: Long,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("updated_at")
    val updatedAt: Long,
)