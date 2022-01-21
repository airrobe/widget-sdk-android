package com.airrobe.widgetsdk.airrobedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airrobe.widgetsdk.airrobewidget.AirRobeWidget
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeOptIn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AirRobeWidget().initialize(
            AirRobeWidgetConfig(
                "c43f2be28f1f",
                "https://www.theiconic.com.au/privacy-policy"
            )
        )
        val airRobeOptIn = findViewById<AirRobeOptIn>(R.id.airriobe_optin_widget)
    }
}