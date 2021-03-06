package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.sessionId
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import org.json.JSONObject
import java.util.concurrent.Executors

internal class AirRobeIdentifyOrderController {
    private val myExecutor = Executors.newSingleThreadExecutor()

    fun start(context: Context, config: AirRobeWidgetConfig, orderId: String, orderOptedIn: Boolean) {
        myExecutor.execute {
            val param = JSONObject()
            param.put("app_id", config.appId)
            param.put("anonymous_id", AirRobeAppUtils.getDeviceId(context))
            param.put("session_id", sessionId)
            param.put("external_order_id", orderId)
            param.put("split_test_variant", "default")
            param.put("opted_in", orderOptedIn)

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

}