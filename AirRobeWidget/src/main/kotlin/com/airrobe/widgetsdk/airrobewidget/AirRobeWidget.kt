package com.airrobe.widgetsdk.airrobewidget

import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.WidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.GetCategoryMappingController

internal val widgetInstance = WidgetInstance

class AirRobeWidget {
    fun initialize(
        config: AirRobeWidgetConfig
    ) {
        widgetInstance.setConfig(config)
        val getCategoryMappingController = GetCategoryMappingController()
        getCategoryMappingController.start(config.appId)
    }
}