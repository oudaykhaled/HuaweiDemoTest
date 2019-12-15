package com.ouday.huawei.profile.presentation.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.ouday.huawei.R
import com.ouday.huawei.core.utils.SecurePreferences
import com.ouday.test.core.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {

    @Inject
    lateinit var pref : SecurePreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, pref.getString("test"), Toast.LENGTH_SHORT).show()
        btnLogin.setOnClickListener {
            pref.put("test", "1")
        }
    }

}
