package com.airrobe.widgetsdk.airrobewidget

import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.CategoryModelInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.GetCategoryMappingController

internal lateinit var configuration: AirRobeWidgetConfig
internal val categoryModelInstance = CategoryModelInstance

class AirRobeWidget {
    fun initialize(
        config: AirRobeWidgetConfig
    ) {
        configuration = config
        val getCategoryMappingController = GetCategoryMappingController()
        getCategoryMappingController.start(config.appId)
    }
}