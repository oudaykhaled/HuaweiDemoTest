package com.ouday.huawei.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.huawei.hms.support.api.hwid.SignInHuaweiId
import com.ouday.huawei.core.utils.SecurePreferences
import com.ouday.huawei.profile.domain.ProfileUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val useCase: ProfileUseCase) : ViewModel() {

    fun saveProfile(pref: SecurePreferences, signInHuaweiId: SignInHuaweiId?) {
        useCase.saveProfile(pref, signInHuaweiId)
    }

    fun getProfile(pref: SecurePreferences): SignInHuaweiId? {
        return useCase.getProfile(pref)
    }

    fun saveVideoEligibility(pref: SecurePreferences, isEligible: Boolean) {
        useCase.saveVideoEligibility(pref, isEligible)
    }

    fun isUserEligible(pref: SecurePreferences): Boolean {
        return useCase.isUserEligible(pref)
    }
}