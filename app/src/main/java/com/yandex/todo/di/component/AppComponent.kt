package com.yandex.todo.di.component

import com.yandex.todo.di.module.AppModule
import com.yandex.todo.di.module.NetworkModule
import com.yandex.todo.di.module.VMModule
import com.yandex.todo.di.module.ViewModelModule
import com.yandex.todo.ui.addtask.AddTaskFragment
import com.yandex.todo.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class, VMModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment)
}