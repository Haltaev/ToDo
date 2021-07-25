package com.yandex.todo.data.model.task

import java.io.Serializable
import java.util.*

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val importance: Importance,
    val isActive: Boolean,
    val deadline: Date,
    val createdAt: Date,
    val updatedAt: Date,
) : Serializable