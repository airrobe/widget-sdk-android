package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.os.Handler
import android.os.Looper
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobePriceEngineListener
import org.json.JSONObject
import java.util.concurrent.Executors

internal class AirRobePriceEngineController {
    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())
    var airRobePriceEngineListener: AirRobePriceEngineListener?  = null

    fun start(price: Float, rrp: Float?, category: String, brand: String?, material: String?) {
        myExecutor.execute {
            val response = AirRobeApiService.requestGET(
                AirRobeConstants.PRICE_ENGINE_HOST +
                        "/v1?price=$price&rrp=$rrp&category=$category" +
                        "&brand=$brand&material=$material"
            )

            myHandler.post {
                if (response != null) {
                    val obj = JSONObject(response)
                    parseToModel(obj)
                } else {
                    airRobePriceEngineListener?.onFailedPriceEngineApi()
                }
            }
        }
    }

    private fun parseToModel(jsonObject: JSONObject) {
        val result = jsonObject.getJSONObject("result")
        val resaleValue = result.getInt("resaleValue")
        airRobePriceEngineListener?.onSuccessPriceEngineApi(resaleValue)
    }
}
