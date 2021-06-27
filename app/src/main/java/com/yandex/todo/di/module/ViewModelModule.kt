package com.yandex.todo.di.module

import androidx.lifecycle.ViewModelProvider
import com.yandex.todo.common.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}