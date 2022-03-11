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

object AirRobeWidget {
    private const val TAG = "AirRobeWidget"

    var borderColor: Int = 0
        set(value) {
            field = value
            widgetInstance.borderColor = value
        }
    var textColor: Int = 0
        set(value) {
            field = value
            widgetInstance.textColor = value
        }
    var switchColor: Int = 0
        set(value) {
            field = value
            widgetInstance.switchColor = value
        }
    var arrowColor: Int = 0
        set(value) {
            field = value
            widgetInstance.arrowColor = value
        }
    var linkTextColor: Int = 0
        set(value) {
            field = value
            widgetInstance.linkTextColor = value
        }
    var buttonBorderColor: Int = 0
        set(value) {
            field = value
            widgetInstance.buttonBorderColor = value
        }
    var buttonTextColor: Int = 0
        set(value) {
            field = value
            widgetInstance.buttonTextColor = value
        }
    var separatorColor: Int = 0
        set(value) {
            field = value
            widgetInstance.separatorColor = value
        }

    fun initialize(
        config: AirRobeWidgetConfig
    ) {
        widgetInstance.setConfig(config)
        val getCategoryMappingController = AirRobeGetCategoryMappingController()
        getCategoryMappingController.airRobeGetCategoryMappingListener = object : AirRobeGetCategoryMappingListener {
            override fun onSuccessGetCategoryMappingApi(categoryModel: CategoryModel) {
                widgetInstance.setCategoryModel(categoryModel)
            }

            override fun onFailedGetCategoryMappingApi(error: String?) {
                Log.e(TAG, error ?: "Email Check Api Failed")
            }
        }
        getCategoryMappingController.start(config.appId, config.mode)
    }

    fun resetOptedIn(context: Context) {
        AirRobeSharedPreferenceManager.setOptedIn(context, false)
    }

    fun orderOptedIn(context: Context): Boolean {
        return AirRobeSharedPreferenceManager.getOrderOptedIn(context)
    }
}