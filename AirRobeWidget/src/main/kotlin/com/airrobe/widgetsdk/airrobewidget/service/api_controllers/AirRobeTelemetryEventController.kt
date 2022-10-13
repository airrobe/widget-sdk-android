package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.*
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.sessionId
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.utils.DateUtils.toIsoString
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.concurrent.Executors

internal class AirRobeTelemetryEventController {
    private val TAG = "Telemetry Event"
    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())

    fun start(
        context: Context,
        config: AirRobeWidgetConfig,
        eventName: String,
        pageName: String,
        brand: String? = null,
        material: String? = null,
        category: String? = null,
        department: String? = null,
        itemCount: Int? = null
    ) {
        myExecutor.execute {
            val param = JSONObject()
            param.put("app_id", config.appId)
            param.put("anonymous_id", AirRobeAppUtils.getDeviceId(context))
            param.put("session_id", sessionId)
            param.put("event_name", eventName)
            param.put("created_at", Date().toIsoString())
            val properties = JSONObject()
            properties.put("source", "Android")
            properties.put("version", context.getString(R.string.airrobe_widget_version))
            properties.put("split_test_variant", AirRobeSharedPreferenceManager.getSplitTestVariant(context)?.splitTestVariant ?: "default")
            properties.put("page_name", pageName)
            if (!brand.isNullOrEmpty()) properties.put("brand", brand)
            if (!material.isNullOrEmpty()) properties.put("material", material)
            if (!category.isNullOrEmpty()) properties.put("category", category)
            if (!department.isNullOrEmpty()) properties.put("department", department)
            if (itemCount != null) properties.put("itemCount", itemCount)
            param.put("properties", properties)

            val response = AirRobeApiService.requestPUT(
                if (config.mode == Mode.PRODUCTION)
                    TelemetryEventHost.Production.raw + "/v1"
                else
                    TelemetryEventHost.Sandbox.raw + "/v1",
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
