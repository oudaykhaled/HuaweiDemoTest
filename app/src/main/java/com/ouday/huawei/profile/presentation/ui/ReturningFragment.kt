package com.ouday.huawei.profile.presentation.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nostra13.universalimageloader.core.ImageLoader

import com.ouday.huawei.R
import com.ouday.huawei.core.utils.Auth
import com.ouday.huawei.core.utils.HuaweiSdkWrapper
import com.ouday.huawei.core.utils.SecurePreferences
import com.ouday.huawei.play.VideoPlayerActivity
import com.ouday.huawei.core.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_returning.*
import javax.inject.Inject

class ReturningFragment : BaseFragment() {

    @Inject
    lateinit var pref : SecurePreferences

    @Inject
    lateinit var huaweiSdkWrapper : HuaweiSdkWrapper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_returning, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        huaweiSdkWrapper
            .setOnPaymentSuccessfully { openVideoActivity() }
            .setOnPaymentFailed { Toast.makeText(activity, "Payment Failed", Toast.LENGTH_SHORT).show() }


        Auth.getProfile(pref)?.let {user ->
            tvWelcome.text = "Welcome, ${user.displayName}"
            ImageLoader.getInstance().displayImage(user.photoUriString, ivProfile )
        }

        ivPlay.setOnClickListener {
            if (Auth.isUserEligible(pref)){
                openVideoActivity()
            }else {
                activity?.let { it1 -> huaweiSdkWrapper.buy(it1, "ConsumeProduct1001", 1) }
            }
        }

    }

    private fun openVideoActivity() {
        val intent = Intent(activity, VideoPlayerActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        huaweiSdkWrapper.onActivityResult(requestCode, resultCode, data)
    }

}
