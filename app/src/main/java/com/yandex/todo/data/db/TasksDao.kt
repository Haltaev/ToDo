package com.yandex.todo.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yandex.todo.data.model.task.TaskDaoEntity

@Dao
interface TasksDao {

    @Query("SELECT * FROM Tasks WHERE is_deleted = 0 ORDER BY is_active ASC, deadline ASC ")
    fun observeTasks(): LiveData<List<TaskDaoEntity>>

    @Query("SELECT * FROM Tasks  WHERE is_active = 1 AND is_deleted = 0 OR has_recent_changes = 1 ORDER BY deadline ASC")
    fun observeActiveTasks(): LiveData<List<TaskDaoEntity>>

    @Query("SELECT * FROM Tasks  WHERE deadline < :endOfDay AND deadline > :currentTime ")
    fun observeActiveTasksForToday(currentTime: Long, endOfDay: Long): List<TaskDaoEntity>

    @Query("SELECT * FROM Tasks")
    suspend fun getTasks(): List<TaskDaoEntity>

    @Query("UPDATE tasks SET has_recent_changes = 0")
    suspend fun updateRecentChangesStatus()

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): TaskDaoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskDaoEntity)

    @Query("UPDATE tasks SET is_active = 1, has_recent_changes = 1 WHERE id = :taskId")
    suspend fun activateTask(taskId: String)

    @Query("UPDATE tasks SET is_active = 0, has_recent_changes = 1 WHERE id = :taskId")
    suspend fun completeTask(taskId: String)

    @Query("UPDATE tasks SET is_deleted = 1, is_dirty = 1 WHERE id = :taskId")
    suspend fun markAsDeleted(taskId: String)

    @Query("UPDATE tasks SET is_deleted = 0 WHERE id = :taskId")
    suspend fun unMarkAsDeleted(taskId: String)

    @Query("UPDATE tasks SET is_dirty = 0 WHERE id = :taskId")
    suspend fun unMarkAsDirty(taskId: String)

    @Query("DELETE FROM Tasks WHERE is_deleted = 1 AND is_dirty = 0")
    suspend fun removeDeletedTask()
}