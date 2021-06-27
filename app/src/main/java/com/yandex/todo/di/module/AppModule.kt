package com.yandex.todo.di.module

import android.content.Context
import com.google.gson.Gson
import com.yandex.todo.App
import com.yandex.todo.data.db.dao.TaskDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {
    @Provides
    internal fun provideContext(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun favoritesDao(): TaskDao {
        return application.tasks
    }

}