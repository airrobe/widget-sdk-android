package com.airrobe.widgetsdk.airrobewidget.service.listeners

interface AirRobePriceEngineListener {
    fun onSuccessPriceEngineApi(resaleValue: Int?)
    fun onFailedPriceEngineApi(error: String? = null)
}