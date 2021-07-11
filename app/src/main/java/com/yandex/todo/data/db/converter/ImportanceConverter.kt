package com.yandex.todo.data.db.converter

import androidx.room.TypeConverter
import com.yandex.todo.data.api.model.task.Importance

class ImportanceConverter {
    @TypeConverter
    fun fromImportance(importance: Importance): String {
        return when(importance) {
            Importance.LOW -> Importance.LOW.type
            Importance.BASIC -> Importance.BASIC.type
            Importance.IMPORTANT -> Importance.IMPORTANT.type
        }
    }

    @TypeConverter
    fun toHobbies(string: String): Importance {
        return Importance.valueOf(string)
    }
}