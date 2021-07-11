package com.yandex.todo.di.module

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.yandex.todo.App
import com.yandex.todo.data.api.repository.DefaultTasksRepository
import com.yandex.todo.data.api.repository.TasksRepository
import com.yandex.todo.data.db.TasksDatabase
import com.yandex.todo.data.db.datasource.TasksDataSource
import com.yandex.todo.data.db.datasource.TasksLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
class AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TasksRemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TasksLocalDataSource

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @TasksLocalDataSource
    @Provides
    fun provideTasksLocalDataSource(
        database: TasksDatabase,
        ioDispatcher: CoroutineDispatcher
    ): TasksDataSource {
        return TasksLocalDataSource(
            database.taskDao(), ioDispatcher
        )
    }

    //    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): TasksDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
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
    abstract fun bindRepository(repo: DefaultTasksRepository): TasksRepository
}
