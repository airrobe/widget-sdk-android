package com.airrobe.widgetsdk.airrobewidget.service.listeners

interface AirRobeEmailCheckListener {
    fun onSuccessEmailCheckApi(isCustomer: Boolean)
    fun onFailedEmailCheckApi(error: String? = null)
}