package com.airrobe.widgetsdk.airrobewidget.service.listeners

internal interface AirRobePriceEngineListener {
    fun onSuccessPriceEngineApi(resaleValue: Int?)
    fun onFailedPriceEngineApi(error: String? = null)
}