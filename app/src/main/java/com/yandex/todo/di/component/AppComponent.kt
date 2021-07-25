package com.yandex.todo.di.component

import android.content.Context
import com.yandex.todo.di.module.*
import com.yandex.todo.ui.addtask.AddEditTaskFragment
import com.yandex.todo.ui.home.HomeFragment
import com.yandex.todo.worker.NotifyWorker
import com.yandex.todo.worker.SyncWorker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        HomeModule::class,
        AddEditTaskModule::class,
        ViewModelModule::class,
        ApplicationModuleBinds::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(homeFragment: HomeFragment)
    fun inject(addEditTaskFragment: AddEditTaskFragment)
    fun inject(homeFragment: NotifyWorker)
    fun inject(syncWorker: SyncWorker)
}