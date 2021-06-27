package com.yandex.todo.di.module

import androidx.lifecycle.ViewModel
import com.traidingviewer.di.ViewModelKey
import com.yandex.todo.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VMModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun homeViewModel(viewModel: HomeViewModel): ViewModel
}
