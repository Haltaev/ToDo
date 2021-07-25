package com.yandex.todo.di.module

import androidx.lifecycle.ViewModel
import com.yandex.todo.di.ViewModelKey
import com.yandex.todo.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel
}