package com.airrobe.widgetsdk.airrobedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airrobe.widgetsdk.airrobewidget.AirRobeWidget
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AirRobeWidget().initialize(
            AirRobeWidgetConfig(
                "123",
                ""
            )
        )
    }
}