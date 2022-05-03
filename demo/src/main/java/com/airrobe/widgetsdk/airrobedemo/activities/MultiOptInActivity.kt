package com.airrobe.widgetsdk.airrobedemo.activities

import android.graphics.Color
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
            arrayListOf("Accessories", "Accessories")
        )
        // Way to set widget colors
//        optInWidget.borderColor = Color.BLUE
//        optInWidget.textColor = Color.BLUE
//        optInWidget.switchColor = Color.BLUE
//        optInWidget.arrowColor = Color.BLUE
//        optInWidget.linkTextColor = Color.BLUE
    }
}