package com.airrobe.widgetsdk.airrobedemo

import android.app.Application
import com.airrobe.widgetsdk.airrobewidget.AirRobeWidget
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.Mode

class ApplicationController: Application() {
    override fun onCreate() {
        super.onCreate()
        AirRobeWidget().initialize(
            AirRobeWidgetConfig(
                "c43f2be28f1f",
                "https://www.theiconic.com.au/privacy-policy",
                mode = Mode.PRODUCTION
            )
        )
    }
}