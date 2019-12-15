package com.ouday.huawei.profile.presentation.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.huawei.hms.support.api.hwid.SignInHuaweiId

import com.ouday.huawei.R
import com.ouday.huawei.core.utils.Auth
import com.ouday.huawei.core.utils.HuaweiSdkWrapper
import com.ouday.huawei.core.utils.SecurePreferences
import com.ouday.huawei.core.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {


    @Inject
    lateinit var pref : SecurePreferences

    @Inject
    lateinit var huaweiSdkWrapper : HuaweiSdkWrapper

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        huaweiSdkWrapper.init()
        initListeners()


        btnLogin.setOnClickListener {
            huaweiSdkWrapper.signin(this)
        }

        if (Auth.getProfile(pref) != null){
            navController.navigate(R.id.action_loginFragment_to_returningFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        huaweiSdkWrapper.onActivityResult(requestCode, resultCode, data)
    }

    private fun initListeners() {
        huaweiSdkWrapper
            .setOnLoginSuccessfully { onLoginSuccessfully(it) }
            .setOnLoginFailed { onFailedToLogin()  }
    }

    private fun onFailedToLogin() {
        Auth.saveProfile(pref, null)
    }

    private fun onLoginSuccessfully(signInHuaweiId: SignInHuaweiId) {
        Auth.saveProfile(pref, signInHuaweiId)
        navController.navigate(R.id.action_loginFragment_to_returningFragment)

    }

}
