package com.bogdanmurzin.mypersonalwallet.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bogdanmurzin.mypersonalwallet.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }, SPLASH_SCREEN_DURATION)
    }

    companion object {
        const val SPLASH_SCREEN_DURATION = 500L
    }
}