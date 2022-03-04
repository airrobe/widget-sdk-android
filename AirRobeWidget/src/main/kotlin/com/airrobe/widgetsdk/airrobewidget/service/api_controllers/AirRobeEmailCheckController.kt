package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeEmailCheckListener
import com.airrobe.widgetsdk.airrobewidget.service.models.EmailCheckResponseModel
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class AirRobeEmailCheckController : Callback<String> {
    var airRobeEmailCheckListener: AirRobeEmailCheckListener? = null
    fun start(email: String) {
        val retrofit = AirRobeApiService.EMAIL_CHECK_SERVICE
        val param = JSONObject()
        param.put(
            "query",
            """
                query IsCustomer {
                  isCustomer(email: "$email")
                }
            """
        )
        retrofit.emailCheck(param.toString()).enqueue(this)
    }

    override fun onResponse(call: Call<String>, response: Response<String>) {
        if (response.isSuccessful) {
            val gson = Gson()
            val result = gson.fromJson(response.body(), EmailCheckResponseModel::class.java)
            airRobeEmailCheckListener?.onSuccessEmailCheckApi(result.data.isCustomer)
        } else {
            val text = response.errorBody()?.string()
            airRobeEmailCheckListener?.onFailedEmailCheckApi(text)
        }
    }

    override fun onFailure(call: Call<String>, t: Throwable) {
        airRobeEmailCheckListener?.onFailedEmailCheckApi()
    }
}
