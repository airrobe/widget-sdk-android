package com.airrobe.widgetsdk.airrobewidget.service.listeners

import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeCreateOptedOutOrderModel

internal interface AirRobeCreateOptedOutOrderListener {
    fun onSuccessCreateOptedOutOrderApi(data: AirRobeCreateOptedOutOrderModel)
    fun onFailedCreateOptedOutOrderApi(error: String? = null)
}