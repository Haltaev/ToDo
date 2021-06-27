package com.yandex.todo

import android.app.Application
import com.yandex.todo.data.db.TaskDatabase
import com.yandex.todo.di.component.AppComponent
import com.yandex.todo.di.component.DaggerAppComponent
import com.yandex.todo.di.module.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val taskDatabase by lazy { TaskDatabase.getDatabase(this) }

    val tasks by lazy { taskDatabase.taskDao() }

    companion object {
        private lateinit var component: AppComponent
        private lateinit var app: App

        fun getComponent(): AppComponent {
            return component
        }

        fun buildAppComponent() {
            component = DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        buildAppComponent()
    }
}