package com.airrobe.widgetsdk.airrobewidget

import android.content.Context
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeGetCategoryMappingController
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeMinPriceThresholdsController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetCategoryMappingListener
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeMinPriceThresholdListener
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeCategoryModel
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeMinPriceThresholdsModel
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
        widgetInstance.configuration = config
        val getCategoryMappingController = AirRobeGetCategoryMappingController()
        getCategoryMappingController.airRobeGetCategoryMappingListener = object : AirRobeGetCategoryMappingListener {
            override fun onSuccessGetCategoryMappingApi(categoryModel: AirRobeCategoryModel) {
                widgetInstance.categoryModel = categoryModel
            }

            override fun onFailedGetCategoryMappingApi(error: String?) {
                Log.e(TAG, error ?: "Get Category Mapping Api Failed")
            }
        }
        getCategoryMappingController.start(config.appId, config.mode)

        val getMinPriceThresholdsController = AirRobeMinPriceThresholdsController()
        getMinPriceThresholdsController.airRobeMinPriceThresholdListener = object : AirRobeMinPriceThresholdListener {
            override fun onSuccessMinPriceThresholdsApi(minPriceThresholdModel: AirRobeMinPriceThresholdsModel) {
                widgetInstance.minPriceThresholdsModel = minPriceThresholdModel
            }

            override fun onFailedMinPriceThresholdsApi(error: String?) {
                Log.e(TAG, error ?: "Get Minimum Price Thresholds Api Failed")
            }
        }
        getMinPriceThresholdsController.start(config.mode)
    }

    fun resetOptedIn(context: Context) {
        AirRobeSharedPreferenceManager.setOptedIn(context, false)
    }

    fun orderOptedIn(context: Context): Boolean {
        return AirRobeSharedPreferenceManager.getOrderOptedIn(context)
    }
}