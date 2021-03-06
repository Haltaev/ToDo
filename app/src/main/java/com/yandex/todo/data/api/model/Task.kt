package com.yandex.todo.data.api.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
class Task(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "deadline")
    val deadline: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "priority")
    val priority: String
)