package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.content.Context
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.sessionId
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import org.json.JSONObject

internal class AirRobeIdentifyOrderController {

    fun start(context: Context, config: AirRobeWidgetConfig, orderId: String) {
        val param = JSONObject()
        param.put("app_id", config.appId)
        param.put("anonymous_id", AirRobeAppUtils.getDeviceId(context))
        param.put("session_id", sessionId)
        param.put("external_order_id", orderId)
        param.put("split_test_variant", "default")
        param.put("opted_in", AirRobeSharedPreferenceManager.getOrderOptedIn(context))

        AirRobeApiService.requestPOST(
            if (config.mode == Mode.PRODUCTION)
                AirRobeConstants.AIRROBE_CONNECTOR_PRODUCTION + "/internal_webhooks/identify_order"
            else
                AirRobeConstants.AIRROBE_CONNECTOR_SANDBOX + "/internal_webhooks/identify_order",
            param,
            false
        )
    }

}