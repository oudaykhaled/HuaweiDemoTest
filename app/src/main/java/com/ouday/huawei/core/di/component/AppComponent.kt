package com.ouday.huawei.core.di.component

import android.app.Application
import com.ouday.huawei.core.App

import com.ouday.huawei.core.di.modules.CoroutinesThreadsProvider
import com.ouday.huawei.core.di.builder.ActivityBuilder
import com.ouday.huawei.core.di.builder.FragmentBuilder
import com.ouday.huawei.core.di.modules.ContextModule
import com.ouday.huawei.core.di.modules.NetworkModule
import com.ouday.huawei.core.di.modules.SchedulersModule
import com.ouday.huawei.profile.di.ProfileDomainModule
import com.ouday.huawei.profile.di.ProfilePresentationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        NetworkModule::class, ContextModule::class,
        ActivityBuilder::class,
        FragmentBuilder::class,
        SchedulersModule::class,
        CoroutinesThreadsProvider::class,
        ProfileDomainModule::class,
        ProfilePresentationModule::class
    ]
)
interface AppComponent {

    fun inject(app: App)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}