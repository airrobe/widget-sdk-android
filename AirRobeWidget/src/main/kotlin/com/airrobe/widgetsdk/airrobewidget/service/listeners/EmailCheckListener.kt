package com.airrobe.widgetsdk.airrobewidget.service.listeners

interface EmailCheckListener {
    fun onSuccessEmailCheckApi(isCustomer: Boolean)
    fun onFailedEmailCheckApi(error: String? = null)
}