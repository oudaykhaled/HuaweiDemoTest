package com.ouday.huawei.profile.presentation.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.huawei.hms.support.api.hwid.SignInHuaweiId

import com.ouday.huawei.R
import com.ouday.huawei.core.utils.Auth
import com.ouday.huawei.core.utils.HuaweiSdkWrapper
import com.ouday.huawei.core.utils.SecurePreferences
import com.ouday.huawei.core.presentation.BaseFragment
import com.ouday.huawei.profile.presentation.viewmodel.ProfileViewModel
import com.ouday.test.core.presentation.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {


    private lateinit var viewModel: ProfileViewModel
    @Inject
    lateinit var pref : SecurePreferences

    @Inject
    lateinit var huaweiSdkWrapper : HuaweiSdkWrapper


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(ProfileViewModel::class.java)

        navController = Navigation.findNavController(view)

        huaweiSdkWrapper.init()
        initListeners()


        btnLogin.setOnClickListener {
            showLoading()
            huaweiSdkWrapper.signin(this)
        }

        if (viewModel.getProfile(pref) != null){
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
        dismissLoading()
        viewModel.saveProfile(pref, null)
    }

    private fun onLoginSuccessfully(signInHuaweiId: SignInHuaweiId) {
        dismissLoading()
        viewModel.saveProfile(pref, signInHuaweiId)
        navController.navigate(R.id.action_loginFragment_to_returningFragment)

    }

}
