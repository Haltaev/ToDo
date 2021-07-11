package com.yandex.todo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yandex.todo.data.api.model.task.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: TaskDatabase? = null
//
//        fun getDatabase(context: Context): TaskDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    TaskDatabase::class.java,
//                    "task_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}