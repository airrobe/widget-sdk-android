package com.airrobe.widgetsdk.airrobedemo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.utils.StatusBarTranslucent

class LogosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logos)
        StatusBarTranslucent.setStatusBarTranslucent(this, false)
        StatusBarTranslucent.setStatusBarColor(this)
    }
}