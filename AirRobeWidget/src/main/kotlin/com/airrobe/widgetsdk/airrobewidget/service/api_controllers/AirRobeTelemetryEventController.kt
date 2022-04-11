package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.Executors

internal class AirRobeTelemetryEventController {
    private val TAG = "Telemetry Event"
    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())

    fun start(context: Context, config: AirRobeWidgetConfig, eventName: String, pageName: String) {
        myExecutor.execute {
            val param = JSONObject()
            param.put("app_id", config.appId)
            param.put("anonymous_id", AirRobeAppUtils.getDeviceId(context))
            param.put("session_id", Build.VERSION.CODENAME)
            param.put("event_name", eventName)
            val properties = JSONObject()
            properties.put("source", "Android")
            properties.put("version", context.getString(R.string.airrobe_widget_version))
            properties.put("split_test_variant", "default")
            properties.put("page_name", pageName)
            param.put("properties", properties)

            val response = AirRobeApiService.requestPOST(
                if (config.mode == Mode.PRODUCTION)
                    AirRobeConstants.AIRROBE_CONNECTOR_PRODUCTION + "/telemetry_events"
                else
                    AirRobeConstants.AIRROBE_CONNECTOR_SANDBOX + "/telemetry_events",
                param,
                false
            )

            myHandler.post {
                if (response != null) {
                    val obj = JSONObject(response)
                    parseToModel(obj)
                } else {
                    Log.e(TAG, "Telemetry Event API Failed")
                }
            }
        }
    }

    private fun parseToModel(jsonObject: JSONObject) {
        try {
            val success = jsonObject.getBoolean("success")
            if (success) {
                Log.d(TAG, "Telemetry Event API Succeed")
            } else {
                Log.e(TAG, "Telemetry Event API Failed")
            }
        } catch (exception: JSONException) {
            Log.e(TAG, "Telemetry Event API Failed: " + exception.localizedMessage)
        }
    }
}
