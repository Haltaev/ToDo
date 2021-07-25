package com.yandex.todo

import android.app.Application
import androidx.work.*
import com.yandex.todo.di.component.AppComponent
import com.yandex.todo.di.component.DaggerAppComponent
import com.yandex.todo.worker.NotifyWorker
import com.yandex.todo.worker.NotifyWorker.Companion.NOTIFY_WORKER_NAME
import com.yandex.todo.worker.SyncWorker
import com.yandex.todo.worker.SyncWorker.Companion.SYNC_WORKER_NAME
import java.util.concurrent.TimeUnit

class App : Application() {
    private lateinit var component: AppComponent

    fun getComponent(): AppComponent {
        return component
    }

    private fun buildAppComponent() {
        component = DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        buildAppComponent()
        val notifyWork = OneTimeWorkRequestBuilder<NotifyWorker>()
            .build()
        WorkManager.getInstance(this).enqueueUniqueWork(
            NOTIFY_WORKER_NAME,
            ExistingWorkPolicy.KEEP,
            notifyWork
        )
        val syncWork = PeriodicWorkRequestBuilder<SyncWorker>(8, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SYNC_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncWork
        )
    }
}
