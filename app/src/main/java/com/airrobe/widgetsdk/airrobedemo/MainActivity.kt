package com.airrobe.widgetsdk.airrobedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airrobe.widgetsdk.airrobewidget.AirRobeWidget
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeMultiOptIn
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
        val airRobeMultiOptIn = findViewById<AirRobeMultiOptIn>(R.id.airrobe_multi_optin_widget)
        airRobeMultiOptIn.initialize(arrayOf("Accessories", "Accessories"))
    }
}