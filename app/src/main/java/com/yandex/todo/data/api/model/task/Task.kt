package com.yandex.todo.data.api.model.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.yandex.todo.data.db.converter.DateConverter
import com.yandex.todo.data.db.converter.ImportanceConverter
import java.util.*

@Entity(tableName = "tasks")
@TypeConverters(ImportanceConverter::class, DateConverter::class)
data class Task(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "importance") var importance: Importance,
    @ColumnInfo(name = "status") var isDone: Boolean,
    @ColumnInfo(name = "deadline")  var deadline: Date,
    @ColumnInfo(name = "created_at")  val createdAt: Date,
    @ColumnInfo(name = "updated_at")  var updatedAt: Date,
)