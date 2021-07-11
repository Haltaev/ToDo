package com.yandex.todo.data.api.model.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class TaskModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("importance")
    val importance: Importance,
    @SerializedName("done")
    val done: Boolean,
    @SerializedName("deadline")
    val deadline: Long,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("updated_at")
    val updatedAt: Long,
) : Serializable, BaseTask {

    companion object {
        const val TASK = "task"
    }
}