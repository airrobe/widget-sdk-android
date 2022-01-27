package com.airrobe.widgetsdk.airrobedemo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeOptIn

class OptInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opt_in)
        val optInWidget = findViewById<AirRobeOptIn>(R.id.opt_in_widget)
        optInWidget.initialize(
            category = "Accessories",
            priceCents = 120f
        )
    }
}