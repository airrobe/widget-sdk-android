package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.widgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

internal class GetCategoryMappingController {
    companion object {
        private const val TAG = "GetCategoryMappingAPI"
    }

    fun start(appId: String) {
        val retrofit = AirRobeApiService.categoryMappingService
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
        GlobalScope.launch {
            try {
                val response = retrofit.getCategoryMapping(param.toString())
                val gson = Gson()
                val categoryModel = gson.fromJson(response.body(), CategoryModel::class.java)
                widgetInstance.setCategoryModel(categoryModel)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.e(TAG, "Failed to get category mapping: " + e.localizedMessage)
            }
        }
    }
}