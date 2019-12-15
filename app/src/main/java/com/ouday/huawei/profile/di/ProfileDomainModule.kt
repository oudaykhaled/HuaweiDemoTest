package com.ouday.huawei.profile.di

import com.ouday.huawei.profile.data.repository.ProfileRepository
import com.ouday.huawei.profile.data.repository.ProfileRepositoryImpl
import com.ouday.huawei.profile.domain.ProfileUseCase
import com.ouday.huawei.profile.domain.ProfileUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ProfileDomainModule {

    @Binds
    abstract fun bindProfileUseCase(
        useCaseImpl: ProfileUseCaseImpl
    ): ProfileUseCase

    @Binds
    abstract fun bindRepo(
        repoImpl: ProfileRepositoryImpl
    ): ProfileRepository
}