package com.ouday.huawei.core.di.builder;

import com.ouday.huawei.profile.presentation.ui.LoginFragment;
import com.ouday.huawei.profile.presentation.ui.ReturningFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface FragmentBuilder {

    @ContributesAndroidInjector
    LoginFragment getLoginFragment();

    @ContributesAndroidInjector
    ReturningFragment getReturningFragment();


}
