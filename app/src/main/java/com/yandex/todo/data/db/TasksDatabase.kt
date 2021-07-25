package com.yandex.todo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yandex.todo.data.db.converter.DateConverter
import com.yandex.todo.data.db.converter.ImportanceConverter
import com.yandex.todo.data.model.task.TaskDaoEntity

@Database(entities = [TaskDaoEntity::class], version = 1, exportSchema = false)
@TypeConverters(ImportanceConverter::class, DateConverter::class)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDao
}