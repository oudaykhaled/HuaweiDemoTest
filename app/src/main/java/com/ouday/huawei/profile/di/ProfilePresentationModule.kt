package com.ouday.huawei.profile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ouday.huawei.core.di.modules.ViewModelKey
import com.ouday.huawei.profile.presentation.viewmodel.ProfileViewModel
import com.ouday.test.core.presentation.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfilePresentationModule {

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindsViewModel(viewModel: ProfileViewModel): ViewModel

}