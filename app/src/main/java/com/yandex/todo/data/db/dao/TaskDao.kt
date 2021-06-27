package com.yandex.todo.data.db.dao

import androidx.room.*
import com.yandex.todo.data.api.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    suspend fun getTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}