package com.airrobe.widgetsdk.airrobewidget

import android.content.Context
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.WidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.GetCategoryMappingController
import com.airrobe.widgetsdk.airrobewidget.utils.SharedPreferenceManager

internal val widgetInstance = WidgetInstance

object AirRobeWidget {
    fun initialize(
        config: AirRobeWidgetConfig
    ) {
        widgetInstance.setConfig(config)
        val getCategoryMappingController = GetCategoryMappingController()
        getCategoryMappingController.start(config.appId)
    }

    fun resetOptedIn(context: Context) {
        SharedPreferenceManager.setOptedIn(context, false)
    }

    fun orderOptedIn(context: Context): Boolean {
        return SharedPreferenceManager.getOrderOptedIn(context)
    }
}