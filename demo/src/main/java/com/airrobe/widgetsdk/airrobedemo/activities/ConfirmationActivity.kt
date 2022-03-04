package com.airrobe.widgetsdk.airrobedemo.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeConfirmation

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        val confirmationWidget = findViewById<AirRobeConfirmation>(R.id.confirmation_widget)
        confirmationWidget.initialize(
            orderId = "123456",
            email = "raj@airrobe.com"
        )
        // Way to set widget colors
//        confirmationWidget.borderColor = Color.BLUE
//        confirmationWidget.textColor = Color.YELLOW
//        confirmationWidget.buttonBackgroundColor = Color.GREEN
//        confirmationWidget.buttonTextColor = Color.GRAY
    }
}