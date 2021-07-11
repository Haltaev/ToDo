package com.yandex.todo.di.module

import androidx.lifecycle.ViewModel
import com.yandex.todo.di.ViewModelKey
import com.yandex.todo.ui.addtask.AddEditTaskFragment
import com.yandex.todo.ui.addtask.AddEditTaskViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AddEditTaskModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    internal abstract fun addEditTaskFragment(): AddEditTaskFragment

    @Binds
    @IntoMap
    @ViewModelKey(AddEditTaskViewModel::class)
    internal abstract fun bindViewModel(viewModel: AddEditTaskViewModel): ViewModel
}
