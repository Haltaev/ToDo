package com.yandex.todo.di.module

import androidx.lifecycle.ViewModel
import com.yandex.todo.di.ViewModelKey
import com.yandex.todo.ui.addtask.AddEditTaskViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditTaskModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddEditTaskViewModel::class)
    internal abstract fun bindViewModel(viewModel: AddEditTaskViewModel): ViewModel
}
