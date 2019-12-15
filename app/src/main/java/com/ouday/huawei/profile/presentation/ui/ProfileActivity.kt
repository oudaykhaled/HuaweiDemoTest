package com.ouday.huawei.profile.presentation.ui

import android.content.Intent
import android.os.Bundle
import com.ouday.huawei.R
import dagger.android.support.DaggerAppCompatActivity


class ProfileActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val fragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
        fragment?.let {
            if ( it is ReturningFragment ) it.onActivityResult(requestCode, resultCode, data)
        }

    }

}
