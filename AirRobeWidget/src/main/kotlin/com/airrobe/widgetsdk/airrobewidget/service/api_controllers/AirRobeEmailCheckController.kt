package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.os.Handler
import android.os.Looper
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeEmailCheckListener
import org.json.JSONObject
import java.util.concurrent.Executors

internal class AirRobeEmailCheckController {
    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())
    var airRobeEmailCheckListener: AirRobeEmailCheckListener? = null

    fun start(email: String) {
        myExecutor.execute {
            val param = JSONObject()
            param.put(
                "query",
                """
                query IsCustomer {
                  isCustomer(email: "$email")
                }
            """
            )

            val response = AirRobeApiService.requestPOST(
                AirRobeConstants.EMAIL_CHECK_HOST + "/graphql",
                param
            )

            myHandler.post {
                if (response != null) {
                    val obj = JSONObject(response)
                    parseToModel(obj)
                } else {
                    airRobeEmailCheckListener?.onFailedEmailCheckApi()
                }
            }
        }
    }

    private fun parseToModel(jsonObject: JSONObject) {
        val data = jsonObject.getJSONObject("data")
        val isCustomer = data.getBoolean("isCustomer")
        airRobeEmailCheckListener?.onSuccessEmailCheckApi(isCustomer)
    }
}
