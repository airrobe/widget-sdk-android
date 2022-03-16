package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeMinPriceThresholdListener
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeMinPriceThresholdsModel
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class AirRobeMinPriceThresholdsController : Callback<String> {
    var airRobeMinPriceThresholdListener: AirRobeMinPriceThresholdListener? = null
    fun start(mode: Mode) {
        val retrofit = if (mode == Mode.PRODUCTION) AirRobeApiService.MAIN_SERVICE_PRODUCTION else AirRobeApiService.MAIN_SERVICE_SANDBOX
        val param = JSONObject()
        param.put(
            "query",
            """
                query GetMPTs {
                  shop {
                    minimumPriceThresholds {
                      minimumPriceCents
                      id
                      department
                      default
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
            val result = gson.fromJson(response.body(), AirRobeMinPriceThresholdsModel::class.java)
            airRobeMinPriceThresholdListener?.onSuccessMinPriceThresholdsApi(result)
        } else {
            val text = response.errorBody()?.string()
            airRobeMinPriceThresholdListener?.onFailedMinPriceThresholdsApi(text)
        }
    }

    override fun onFailure(call: Call<String>, t: Throwable) {
        airRobeMinPriceThresholdListener?.onFailedMinPriceThresholdsApi()
    }
}