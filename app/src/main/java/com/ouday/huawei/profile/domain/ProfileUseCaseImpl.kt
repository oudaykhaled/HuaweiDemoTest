package com.ouday.huawei.profile.domain

import com.huawei.hms.support.api.hwid.SignInHuaweiId
import com.ouday.huawei.core.utils.SecurePreferences
import com.ouday.huawei.profile.data.repository.ProfileRepository
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(private val repository: ProfileRepository): ProfileUseCase {

    override fun saveProfile(pref: SecurePreferences, signInHuaweiId: SignInHuaweiId?) {
        repository.saveProfile(pref, signInHuaweiId)
    }

    override fun getProfile(pref: SecurePreferences): SignInHuaweiId? {
        return repository.getProfile(pref)
    }

    override fun saveVideoEligibility(pref: SecurePreferences, isEligible: Boolean) {
        repository.saveVideoEligibility(pref, isEligible)
    }

    override fun isUserEligible(pref: SecurePreferences): Boolean {
        return repository.isUserEligible(pref)
    }
}