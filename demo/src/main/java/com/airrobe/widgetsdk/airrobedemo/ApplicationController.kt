package com.airrobe.widgetsdk.airrobedemo

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.AirRobeWidget
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeEventData
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeEventListener

class ApplicationController : Application(), AirRobeEventListener {
    override fun onCreate() {
        super.onCreate()
        appContext = this

        // Initialize SDK
        AirRobeWidget.initialize(
            AirRobeWidgetConfig(
                "c43f2be28f1f",
                "https://www.theiconic.com.au/privacy-policy"
            )
        )
        AirRobeWidget.eventListener = this

        // Way to set all widgets colors globally
//        AirRobeWidget.borderColor = Color.BLUE
//        AirRobeWidget.arrowColor = Color.GREEN
//        AirRobeWidget.textColor = Color.YELLOW
//        AirRobeWidget.switchColor = Color.CYAN
//        AirRobeWidget.buttonBorderColor = Color.LTGRAY
//        AirRobeWidget.buttonTextColor = Color.MAGENTA
//        AirRobeWidget.separatorColor = Color.RED
//        AirRobeWidget.linkTextColor = Color.GRAY
    }

    companion object {
        private lateinit var appContext: ApplicationController

        @get: Synchronized
        val instance: ApplicationController
            get() {
                var applicationController: ApplicationController
                synchronized(ApplicationController::class.java) {
                    applicationController = appContext
                }
                return applicationController
            }

        fun context(): Context {
            return appContext
        }
    }

    override fun onEventEmitted(event: AirRobeEventData) {
        Log.d("Demo", event.toString())
    }
}