package com.ouday.huawei.core.utils

import com.google.gson.Gson
import com.huawei.hms.support.api.hwid.SignInHuaweiId

object Auth {

    private const val PROFILE = "PROFILE"
    private const val IS_ELIGIBLE_TO_PLAY = "IS_ELIGIBLE_TO_PLAY"

    fun saveProfile(pref: SecurePreferences, signInHuaweiId: SignInHuaweiId?){
        pref.put(PROFILE, Gson().toJson(signInHuaweiId))
    }

    fun getProfile(pref: SecurePreferences): SignInHuaweiId? {
        return Gson().fromJson(pref.getString(PROFILE), SignInHuaweiId::class.java)
    }

    fun saveVideoEligibility(pref: SecurePreferences, isEligible: Boolean){
        pref.put(IS_ELIGIBLE_TO_PLAY, if (isEligible) "1" else "0")
    }

    fun isUserEligible(pref: SecurePreferences): Boolean {
        return pref.getString(IS_ELIGIBLE_TO_PLAY) == "1"
    }

}