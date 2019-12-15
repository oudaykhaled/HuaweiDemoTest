package com.ouday.huawei.profile.data.repository

import com.huawei.hms.support.api.hwid.SignInHuaweiId
import com.ouday.huawei.core.utils.SecurePreferences

interface ProfileRepository {

    fun saveProfile(pref: SecurePreferences, signInHuaweiId: SignInHuaweiId?)

    fun getProfile(pref: SecurePreferences): SignInHuaweiId?

    fun saveVideoEligibility(pref: SecurePreferences, isEligible: Boolean)

    fun isUserEligible(pref: SecurePreferences): Boolean

}