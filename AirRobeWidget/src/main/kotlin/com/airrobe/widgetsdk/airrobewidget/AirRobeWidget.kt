package com.airrobe.widgetsdk.airrobewidget

import android.content.Context
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeGetCategoryMappingController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetCategoryMappingListener
import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager

internal val widgetInstance = AirRobeWidgetInstance

object AirRobeWidget : AirRobeGetCategoryMappingListener {
    private const val TAG = "AirRobeWidget"

    fun initialize(
        config: AirRobeWidgetConfig
    ) {
        widgetInstance.setConfig(config)
        val getCategoryMappingController = AirRobeGetCategoryMappingController()
        getCategoryMappingController.airRobeGetCategoryMappingListener = this
        getCategoryMappingController.start(config.appId)
    }

    fun resetOptedIn(context: Context) {
        AirRobeSharedPreferenceManager.setOptedIn(context, false)
    }

    fun orderOptedIn(context: Context): Boolean {
        return AirRobeSharedPreferenceManager.getOrderOptedIn(context)
    }

    override fun onSuccessGetCategoryMappingApi(categoryModel: CategoryModel) {
        widgetInstance.setCategoryModel(categoryModel)
    }

    override fun onFailedGetCategoryMappingApi(error: String?) {
        Log.e(TAG, error ?: "Email Check Api Failed")
    }
}