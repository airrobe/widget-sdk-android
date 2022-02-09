package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobePriceEngineListener
import com.airrobe.widgetsdk.airrobewidget.service.models.PriceEngineResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AirRobePriceEngineController : Callback<PriceEngineResponseModel> {
    var airRobePriceEngineListener: AirRobePriceEngineListener?  =null
    fun start(price: Float, rrp: Float?, category: String, brand: String?, material: String?) {
        val retrofit = AirRobeApiService.PRICE_ENGINE_SERVICE
        retrofit.priceEngine(price, rrp, category, brand, material).enqueue(this)
    }

    override fun onResponse(call: Call<PriceEngineResponseModel>, response: Response<PriceEngineResponseModel>) {
        if (response.isSuccessful) {
            val result = response.body() as PriceEngineResponseModel
            airRobePriceEngineListener?.onSuccessPriceEngineApi(result.result?.resaleValue)
        } else {
            val text = response.errorBody()?.string()
            airRobePriceEngineListener?.onFailedPriceEngineApi(text)
        }
    }

    override fun onFailure(call: Call<PriceEngineResponseModel>, t: Throwable) {
        airRobePriceEngineListener?.onFailedPriceEngineApi()
    }
}
