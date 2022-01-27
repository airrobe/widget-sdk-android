package com.airrobe.widgetsdk.airrobedemo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeConfirmation
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeMultiOptIn

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        val confirmationWidget = findViewById<AirRobeConfirmation>(R.id.confirmation_widget)
        confirmationWidget.initialize(
            orderId = "123456",
            email = "raj@airrobe.com"
        )
    }
}