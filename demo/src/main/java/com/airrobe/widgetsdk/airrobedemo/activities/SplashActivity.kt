package com.airrobe.widgetsdk.airrobedemo.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.utils.StatusBarTranslucent

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        StatusBarTranslucent.setStatusBarTranslucent(this, true)
        val ivLogo = findViewById<ImageView>(R.id.iv_logo)
        val scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_in)
        ivLogo.startAnimation(scaleAnim)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, BrandActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}