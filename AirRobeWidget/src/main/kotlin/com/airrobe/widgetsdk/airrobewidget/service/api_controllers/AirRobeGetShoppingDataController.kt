package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.os.Handler
import android.os.Looper
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetShoppingDataListener
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeGetShoppingDataModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.util.concurrent.Executors

internal class AirRobeGetShoppingDataController {
    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())
    var airRobeGetShoppingDataListener: AirRobeGetShoppingDataListener? = null

    fun start(appId: String, mode: Mode) {
        myExecutor.execute {
            val param = JSONObject()
            param.put(
                "query",
                """
                query GetShoppingData {
                  shop(appId: "$appId") {
                    name
                    privacyUrl
                    popupFindOutMoreUrl
                    categoryMappings(mappedOrExcludedOnly: true) {
                      from
                      to
                      excluded
                    }
                    minimumPriceThresholds {
                      default
                      department
                      minimumPriceCents
                    }
                    widgetVariants {
                      disabled
                      splitTestVariant
                    }
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
                    try {
                        val data =
                            Gson().fromJson(response, AirRobeGetShoppingDataModel::class.java)
                        airRobeGetShoppingDataListener?.onSuccessGetShoppingDataApi(data)
                    } catch (exception: JsonSyntaxException) {
                        airRobeGetShoppingDataListener?.onFailedGetShoppingDataApi("Get Shopping Data JSON parse issue: " + exception.localizedMessage)
                    }
                } else {
                    airRobeGetShoppingDataListener?.onFailedGetShoppingDataApi()
                }
            }
        }
    }
}