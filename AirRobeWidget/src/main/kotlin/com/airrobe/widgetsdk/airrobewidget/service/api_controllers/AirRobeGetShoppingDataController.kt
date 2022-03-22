package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetShoppingDataListener
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeGetShoppingDataModel
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class AirRobeGetShoppingDataController : Callback<String> {
    var airRobeGetShoppingDataListener: AirRobeGetShoppingDataListener? = null
    fun start(appId: String, mode: Mode) {
        val retrofit = if (mode == Mode.PRODUCTION) AirRobeApiService.MAIN_SERVICE_PRODUCTION else AirRobeApiService.MAIN_SERVICE_SANDBOX
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
        retrofit.mainGraphQL(param.toString()).enqueue(this)
    }

    override fun onResponse(call: Call<String>, response: Response<String>) {
        if (response.isSuccessful) {
            val gson = Gson()
            val result = gson.fromJson(response.body(), AirRobeGetShoppingDataModel::class.java)
            airRobeGetShoppingDataListener?.onSuccessGetShoppingDataApi(result)
        } else {
            val text = response.errorBody()?.string()
            airRobeGetShoppingDataListener?.onFailedGetShoppingDataApi(text)
        }
    }

    override fun onFailure(call: Call<String>, t: Throwable) {
        airRobeGetShoppingDataListener?.onFailedGetShoppingDataApi()
    }
}