package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.os.Handler
import android.os.Looper
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetShoppingDataListener
import com.airrobe.widgetsdk.airrobewidget.service.models.*
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeCategoryMapping
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeGetShoppingDataModel
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeMinPriceThresholds
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeShopModel
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
                    categoryMappings {
                      from
                      to
                      excluded
                    }
                    minimumPriceThresholds {
                      default
                      department
                      minimumPriceCents
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
                    val obj = JSONObject(response)
                    parseToModel(obj)
                } else {
                    airRobeGetShoppingDataListener?.onFailedGetShoppingDataApi()
                }
            }
        }
    }

    private fun parseToModel(jsonObject: JSONObject) {
        val data = jsonObject.getJSONObject("data")
        val shop = data.getJSONObject("shop")
        val categoryMappings = shop.getJSONArray("categoryMappings")
        val minimumPriceThresholds = shop.getJSONArray("minimumPriceThresholds")
        val categoryMappingsArray: MutableList<AirRobeCategoryMapping> = arrayListOf()
        for (i in 0 until categoryMappings.length()) {
            val categoryMapping = categoryMappings.getJSONObject(i)
            val from = categoryMapping.getString("from")
            val to = categoryMapping.getString("to")
            val excluded = categoryMapping.getBoolean("excluded")
            categoryMappingsArray.add(
                AirRobeCategoryMapping(
                    from, to, excluded
                )
            )
        }
        val minimumPriceThresholdsArray: MutableList<AirRobeMinPriceThresholds> = arrayListOf()
        for (i in 0 until minimumPriceThresholds.length()) {
            val minimumPriceThreshold = minimumPriceThresholds.getJSONObject(i)
            val minimumPriceCents = minimumPriceThreshold.getDouble("minimumPriceCents")
            val department = minimumPriceThreshold.getString("department")
            val default = minimumPriceThreshold.getBoolean("default")
            minimumPriceThresholdsArray.add(
                AirRobeMinPriceThresholds(
                    minimumPriceCents, department, default
                )
            )
        }
        val dataModel = AirRobeGetShoppingDataModel(
            AirRobeShoppingDataModel(
                AirRobeShopModel(
                    categoryMappingsArray, minimumPriceThresholdsArray
                )
            )
        )
        airRobeGetShoppingDataListener?.onSuccessGetShoppingDataApi(dataModel)
    }
}