package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.os.Handler
import android.os.Looper
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeCreateOptedOutOrderListener
import com.airrobe.widgetsdk.airrobewidget.service.models.*
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.Executors

internal class AirRobeCreateOptedOutOrderController {
    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())
    var airRobeCreateOptedOutOrderListener: AirRobeCreateOptedOutOrderListener? = null

    fun start(orderId: String, appId: String, orderSubTotalCents: Int, currency: String, mode: Mode) {
        myExecutor.execute {
            val input = JSONObject()
            input.put("id", orderId)
            input.put("shopAppId", appId)
            val subTotal = JSONObject()
            subTotal.put("cents", orderSubTotalCents)
            subTotal.put("currency", currency)
            input.put("subtotal", subTotal)
            val param = JSONObject()
            param.put(
                "query",
                """
                mutation CreateOptedOutOrder {
                  createOptedOutOrder(input: "$input") {
                    created
                    error
                  }
                }
            """
            )

            val response = AirRobeApiService.requestPOST(
                if (mode == Mode.PRODUCTION)
                    AirRobeConstants.AIRROBE_CONNECTOR_PRODUCTION + "/graphql"
                else
                    AirRobeConstants.AIRROBE_CONNECTOR_SANDBOX + "/graphql",
                param
            )

            myHandler.post {
                if (response != null) {
                    val obj = JSONObject(response)
                    parseToModel(obj)
                } else {
                    airRobeCreateOptedOutOrderListener?.onFailedCreateOptedOutOrderApi()
                }
            }
        }
    }

    private fun parseToModel(jsonObject: JSONObject) {
        try {
            val data = jsonObject.getJSONObject("data")
            val createOptedOutOrder = data.getJSONObject("createOptedOutOrder")
            val created = createOptedOutOrder.getBoolean("created")
            val error = createOptedOutOrder.getString("error")
            val dataModel = AirRobeCreateOptedOutOrderModel(
                AirRobeCreateOptedOutOrderDataModel(
                    AirRobeCreateOptedOutOrderSubModel(
                        created, error
                    )
                )
            )
            airRobeCreateOptedOutOrderListener?.onSuccessCreateOptedOutOrderApi(dataModel)
        } catch (exception: JSONException) {
            airRobeCreateOptedOutOrderListener?.onFailedCreateOptedOutOrderApi("Create Opted Out Order JSON parse issue: " + exception.localizedMessage)
        }
    }
}