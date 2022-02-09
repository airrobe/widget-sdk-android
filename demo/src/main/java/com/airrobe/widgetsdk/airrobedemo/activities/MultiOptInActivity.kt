package com.airrobe.widgetsdk.airrobedemo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeMultiOptIn

class MultiOptInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_opt_in)
        val optInWidget = findViewById<AirRobeMultiOptIn>(R.id.multi_opt_in_widget)
        optInWidget.initialize(
            arrayOf("Accessories", "Accessories")
        )
    }
}