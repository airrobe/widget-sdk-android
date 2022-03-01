package com.airrobe.widgetsdk.airrobewidget.service.listeners

internal interface AirRobeEmailCheckListener {
    fun onSuccessEmailCheckApi(isCustomer: Boolean)
    fun onFailedEmailCheckApi(error: String? = null)
}