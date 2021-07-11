package com.yandex.todo.data.api.model.task

import com.google.gson.annotations.SerializedName


enum class Importance (val type: String) {
    @SerializedName("low")
    LOW("low"),

    @SerializedName("basic")
    BASIC("basic"),

    @SerializedName("important")
    IMPORTANT("important"),
}