package com.airrobe.widgetsdk.airrobedemo

import android.app.Application
import android.content.Context
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.AirRobeWidget
import com.airrobe.widgetsdk.airrobewidget.config.*

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
        when (event.event_name) {
            EventName.PageView -> Log.d("Demo", "pageview")
            EventName.WidgetNotRendered -> Log.d("Demo", "widget not rendered")
            EventName.OptedIn -> Log.d("Demo", "opted in")
            EventName.OptedOut -> Log.d("Demo", "opted out")
            EventName.WidgetExpand -> Log.d("Demo", "widget expand")
            EventName.WidgetCollapse -> Log.d("Demo", "widget collapse")
            EventName.PopupClick -> Log.d("Demo", "popup click")
            EventName.ClaimLinkClick -> Log.d("Demo", "claim link click")
            EventName.Other -> Log.d("Demo", "Other")
        }

        when (event.page_name) {
            PageName.Product -> Log.d("Demo", "widget on product page")
            PageName.Cart -> Log.d("Demo", "widget on cart page")
            PageName.ThankYou -> Log.d("Demo", "widget on thank you page")
            PageName.Other -> Log.d("Demo", "Other")
        }
    }
}