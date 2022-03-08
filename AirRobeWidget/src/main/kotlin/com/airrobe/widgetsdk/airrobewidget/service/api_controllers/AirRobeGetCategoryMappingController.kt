package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import com.airrobe.widgetsdk.airrobewidget.config.Mode
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetCategoryMappingListener
import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class AirRobeGetCategoryMappingController : Callback<String> {
    var airRobeGetCategoryMappingListener: AirRobeGetCategoryMappingListener? = null
    fun start(appId: String, mode: Mode) {
        val retrofit = if (mode == Mode.PRODUCTION) AirRobeApiService.CATEGORY_MAPPING_SERVICE_PRODUCTION else AirRobeApiService.CATEGORY_MAPPING_SERVICE_SANDBOX
        val param = JSONObject()
        param.put(
            "query",
            """
                query GetMappingInfo {
                  shop(appId: "$appId") {
                    categoryMappings {
                      from
                      to
                      excluded
                    }
                  }
                }
            """
        )
        retrofit.getCategoryMapping(param.toString()).enqueue(this)
    }

    override fun onResponse(call: Call<String>, response: Response<String>) {
        if (response.isSuccessful) {
            val gson = Gson()
            val result = gson.fromJson(response.body(), CategoryModel::class.java)
            airRobeGetCategoryMappingListener?.onSuccessGetCategoryMappingApi(result)
        } else {
            val text = response.errorBody()?.string()
            airRobeGetCategoryMappingListener?.onFailedGetCategoryMappingApi(text)
        }
    }

    override fun onFailure(call: Call<String>, t: Throwable) {
        airRobeGetCategoryMappingListener?.onFailedGetCategoryMappingApi()
    }
}