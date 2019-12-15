package com.ouday.huawei.core.di.builder;

import com.ouday.huawei.MainActivity;
import com.ouday.huawei.profile.presentation.ui.ProfileActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ActivityBuilder {

    @ContributesAndroidInjector
    MainActivity getMainActivity();

    @ContributesAndroidInjector
    ProfileActivity getProfileActivity();

}
