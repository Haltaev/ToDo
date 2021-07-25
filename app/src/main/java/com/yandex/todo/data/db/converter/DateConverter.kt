package com.yandex.todo.data.db.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time

    @TypeConverter
    fun fromTimestamp(value: Long) = Date(value)
}