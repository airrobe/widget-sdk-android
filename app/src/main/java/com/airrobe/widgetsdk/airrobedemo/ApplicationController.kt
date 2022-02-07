package com.airrobe.widgetsdk.airrobedemo

import android.app.Application
import android.content.Context
import com.airrobe.widgetsdk.airrobewidget.AirRobeWidget
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig

class ApplicationController : Application() {
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
}