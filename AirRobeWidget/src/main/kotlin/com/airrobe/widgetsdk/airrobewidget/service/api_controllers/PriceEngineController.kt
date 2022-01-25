package com.airrobe.widgetsdk.airrobewidget.service.api_controllers

import com.airrobe.widgetsdk.airrobewidget.service.AirRobeApiService
import com.airrobe.widgetsdk.airrobewidget.service.listeners.PriceEngineListener
import com.airrobe.widgetsdk.airrobewidget.service.models.PriceEngineResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriceEngineController : Callback<PriceEngineResponseModel> {
    var priceEngineListener: PriceEngineListener?  =null
    fun start(price: Float, rrp: Float?, category: String, brand: String?, material: String?) {
        val retrofit = AirRobeApiService.priceEngineService
        retrofit.priceEngine(price, rrp, category, brand, material).enqueue(this)
    }

    override fun onResponse(call: Call<PriceEngineResponseModel>, response: Response<PriceEngineResponseModel>) {
        if (response.isSuccessful) {
            val result = response.body() as PriceEngineResponseModel
            priceEngineListener?.onSuccessPriceEngineApi(result.result?.resaleValue)
        } else {
            val text = response.errorBody()?.string()
            priceEngineListener?.onFailedPriceEngineApi(text)
        }
    }

    override fun onFailure(call: Call<PriceEngineResponseModel>, t: Throwable) {
        priceEngineListener?.onFailedPriceEngineApi()
    }
}
