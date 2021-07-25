package com.yandex.todo.di.module

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.yandex.todo.data.api.repository.TasksRepository
import com.yandex.todo.data.api.repository.TasksRepositoryImpl
import com.yandex.todo.data.db.TasksDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun providesAppScope() = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideDataBase(applicationContext: Context): TasksDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TasksDatabase::class.java,
            "Tasks.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: TasksRepositoryImpl): TasksRepository
}
