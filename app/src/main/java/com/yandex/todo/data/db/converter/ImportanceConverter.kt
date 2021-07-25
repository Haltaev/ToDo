package com.yandex.todo.data.db.converter

import androidx.room.TypeConverter
import com.yandex.todo.data.model.task.Importance

class ImportanceConverter {
    @TypeConverter
    fun fromImportance(importance: Importance): String = importance.type

    @TypeConverter
    fun toImportance(string: String): Importance = when (string) {
        Importance.LOW.type -> Importance.LOW
        Importance.BASIC.type -> Importance.BASIC
        Importance.IMPORTANT.type -> Importance.IMPORTANT
        else -> Importance.BASIC
    }
}