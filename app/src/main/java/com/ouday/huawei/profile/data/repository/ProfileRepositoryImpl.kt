package com.ouday.huawei.profile.data.repository

import com.huawei.hms.support.api.hwid.SignInHuaweiId
import com.ouday.huawei.core.utils.Auth
import com.ouday.huawei.core.utils.SecurePreferences
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(): ProfileRepository {

    override fun saveProfile(pref: SecurePreferences, signInHuaweiId: SignInHuaweiId?) {
        Auth.saveProfile(pref, signInHuaweiId)
    }

    override fun getProfile(pref: SecurePreferences): SignInHuaweiId? {
        return Auth.getProfile(pref)
    }

    override fun saveVideoEligibility(pref: SecurePreferences, isEligible: Boolean) {
        Auth.saveVideoEligibility(pref, isEligible)
    }

    override fun isUserEligible(pref: SecurePreferences): Boolean {
        return Auth.isUserEligible(pref)
    }

}