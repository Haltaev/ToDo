package com.yandex.todo.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yandex.todo.App
import com.yandex.todo.data.api.repository.TasksRepository
import javax.inject.Inject

class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        const val SYNC_WORKER_NAME = "syncWorkerName"
    }

    @Inject
    lateinit var tasksRepository: TasksRepository

    init {
        (context.applicationContext as App).getComponent().inject(this)
    }

    override suspend fun doWork(): Result {
        tasksRepository.downloadTasks()
        return Result.success()
    }
}