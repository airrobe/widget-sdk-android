package com.airrobe.widgetsdk.airrobewidget

import android.content.Context
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeGetShoppingDataController
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeEventListener
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetShoppingDataListener
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeGetShoppingDataModel
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import kotlin.math.roundToInt

internal val widgetInstance = AirRobeWidgetInstance
internal var sessionId = ""
internal var eventListenerInstance: AirRobeEventListener? = null

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

    var eventListener: AirRobeEventListener? = null
        set(value) {
            field = value
            eventListenerInstance = value
        }

    fun initialize(
        config: AirRobeWidgetConfig
    ) {
        widgetInstance.configuration = config
        sessionId = ((System.currentTimeMillis() / 1000).toDouble().roundToInt()).toString()
        val getShoppingDataController = AirRobeGetShoppingDataController()
        getShoppingDataController.airRobeGetShoppingDataListener = object : AirRobeGetShoppingDataListener {
            override fun onSuccessGetShoppingDataApi(shopModel: AirRobeGetShoppingDataModel) {
                widgetInstance.shopModel = shopModel
                for (i in 0 until shopModel.data.shop.categoryMappings.count()) {
                    widgetInstance.categoryMapping.categoryMappingsHashmap[shopModel.data.shop.categoryMappings[i].from] = shopModel.data.shop.categoryMappings[i]
                }
            }

            override fun onFailedGetShoppingDataApi(error: String?) {
                Log.e(TAG, error ?: "Get Shopping Data Api Failed")
            }
        }
        getShoppingDataController.start(config.appId, config.mode)
    }

    fun trackPageView(context: Context, pageName: String) {
        AirRobeAppUtils.telemetryEvent(context, "pageview", pageName)
    }

    fun checkMultiOptInEligibility(items: ArrayList<String>): Boolean {
        if (widgetInstance.shopModel == null || items.isEmpty() || widgetInstance.categoryMapping.categoryMappingsHashmap.isNullOrEmpty()) {
            return false
        }
        val to = widgetInstance.categoryMapping.checkCategoryEligible(items)
        return to != null
    }

    fun checkConfirmationEligibility(context: Context, orderId: String, email: String, fraudRisk: Boolean): Boolean {
        if (orderId.isEmpty() || email.isEmpty()) {
            return false
        }
        return AirRobeSharedPreferenceManager.getOrderOptedIn(context) && !fraudRisk
    }

    fun resetOptedIn(context: Context) {
        AirRobeSharedPreferenceManager.setOptedIn(context, false)
    }

    fun orderOptedIn(context: Context): Boolean {
        return AirRobeSharedPreferenceManager.getOrderOptedIn(context)
    }
}