package com.airrobe.widgetsdk.airrobewidget.service.listeners

interface PriceEngineListener {
    fun onSuccessPriceEngineApi(resaleValue: Int?)
    fun onFailedPriceEngineApi(error: String? = null)
}