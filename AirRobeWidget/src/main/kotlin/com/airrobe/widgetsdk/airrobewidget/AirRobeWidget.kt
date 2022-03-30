package com.airrobe.widgetsdk.airrobewidget

import android.content.Context
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeGetShoppingDataController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetShoppingDataListener
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeGetShoppingDataModel
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
        val getShoppingDataController = AirRobeGetShoppingDataController()
        getShoppingDataController.airRobeGetShoppingDataListener = object : AirRobeGetShoppingDataListener {
            override fun onSuccessGetShoppingDataApi(shopModel: AirRobeGetShoppingDataModel) {
                widgetInstance.shopModel = shopModel
            }

            override fun onFailedGetShoppingDataApi(error: String?) {
                Log.e(TAG, error ?: "Get Shopping Data Api Failed")
            }
        }
        getShoppingDataController.start(config.appId, config.mode)
    }

    fun checkMultiOptInEligibility(items: Array<CharSequence>): Boolean {
        if (widgetInstance.shopModel == null || items.isNullOrEmpty()) {
            return false
        }

        val newItems = arrayListOf<String>()
        for (item in items) {
            newItems.add(item.toString())
        }
        val to = widgetInstance.shopModel!!.checkCategoryEligible(newItems)
        return to != null
    }

    fun resetOptedIn(context: Context) {
        AirRobeSharedPreferenceManager.setOptedIn(context, false)
    }

    fun orderOptedIn(context: Context): Boolean {
        return AirRobeSharedPreferenceManager.getOrderOptedIn(context)
    }
}