package com.yandex.todo.data.model.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.yandex.todo.data.db.converter.DateConverter
import com.yandex.todo.data.db.converter.ImportanceConverter
import java.util.*

@Entity(tableName = "tasks")
@TypeConverters(ImportanceConverter::class, DateConverter::class)
data class TaskDaoEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "importance") @TypeConverters(ImportanceConverter::class) val importance: Importance,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "deadline") @TypeConverters(DateConverter::class) val deadline: Date,
    @ColumnInfo(name = "created_at") @TypeConverters(DateConverter::class) val createdAt: Date,
    @ColumnInfo(name = "updated_at") @TypeConverters(DateConverter::class) val updatedAt: Date,
    @ColumnInfo(name = "has_recent_changes") val hasRecentChanges: Boolean = false,
    @ColumnInfo(name = "is_dirty") val isDirty: Boolean = true,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean = false,
)