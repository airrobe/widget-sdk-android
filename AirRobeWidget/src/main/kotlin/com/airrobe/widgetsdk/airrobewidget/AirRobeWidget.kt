package com.airrobe.widgetsdk.airrobewidget

import android.app.Application
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig

class AirRobeWidget: Application() {
    var config: AirRobeWidgetConfig? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: AirRobeWidget
            private set
    }

    fun initialize(
        config: AirRobeWidgetConfig
    ) {
        this.config = config
    }
}