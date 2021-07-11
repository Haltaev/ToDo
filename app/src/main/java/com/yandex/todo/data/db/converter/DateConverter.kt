package com.yandex.todo.data.db.converter

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*


class DateConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

//    @TypeConverter
//    fun getDate(timestamp: Long): String {
//        val calendar = Calendar.getInstance(Locale.ENGLISH)
//        calendar.timeInMillis = timestamp * 1000L
//        return DateFormat.format("dd-MM-yyyy", calendar).toString()
//    }

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }
}