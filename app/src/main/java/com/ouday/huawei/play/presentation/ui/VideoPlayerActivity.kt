package com.ouday.huawei.play.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.khizar1556.mkvideoplayer.MKPlayer
import com.ouday.huawei.R

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var player: MKPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        player = MKPlayer(this)
        player.play("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8")
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            player.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (player != null) {
            player.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player != null) {
            player.onDestroy()
        }
    }

    override fun onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}
