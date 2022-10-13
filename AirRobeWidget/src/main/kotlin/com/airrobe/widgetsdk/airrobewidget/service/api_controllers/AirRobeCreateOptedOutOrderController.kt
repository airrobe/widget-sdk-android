package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.os.Handler
import android.os.Looper
import com.airrobe.widgetsdk.airrobewidget.config.Connector
import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeCreateOptedOutOrderListener
import com.airrobe.widgetsdk.airrobewidget.service.models.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.util.concurrent.Executors

internal class AirRobeCreateOptedOutOrderController {
    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())
    var airRobeCreateOptedOutOrderListener: AirRobeCreateOptedOutOrderListener? = null

    fun start(orderId: String, appId: String, orderSubtotalCents: Int, currency: String, mode: Mode) {
        myExecutor.execute {
            val param = JSONObject()
            param.put(
                "query",
                """
                mutation CreateOptedOutOrder(${'$'}input: CreateOptedOutOrderMutationInput!) {
                  createOptedOutOrder(input: ${'$'}input) {
                    created
                    error
                  }
                }
            """
            )

            val inputData = JSONObject()
            inputData.put("id", orderId)
            inputData.put("shopAppId", appId)

            val subTotal = JSONObject()
            subTotal.put("cents", orderSubtotalCents)
            subTotal.put("currency", currency)
            inputData.put("subtotal", subTotal)

            val input = JSONObject()
            input.put("input", inputData)

            param.put(
                "variables",
                input
            )
            param.put(
                "operationName",
                "CreateOptedOutOrder"
            )

            val response = AirRobeApiService.requestPOST(
                if (mode == Mode.PRODUCTION)
                    Connector.Production.raw + "/graphql"
                else
                    Connector.Sandbox.raw + "/graphql",
                param
            )

            myHandler.post {
                if (response != null) {
                    try {
                        val data =
                            Gson().fromJson(response, AirRobeCreateOptedOutOrderModel::class.java)
                        airRobeCreateOptedOutOrderListener?.onSuccessCreateOptedOutOrderApi(data)
                    } catch (exception: JsonSyntaxException) {
                        airRobeCreateOptedOutOrderListener?.onFailedCreateOptedOutOrderApi("Create Opted Out Order JSON parse issue: " + exception.localizedMessage)
                    }
                } else {
                    airRobeCreateOptedOutOrderListener?.onFailedCreateOptedOutOrderApi()
                }
            }
        }
    }
}