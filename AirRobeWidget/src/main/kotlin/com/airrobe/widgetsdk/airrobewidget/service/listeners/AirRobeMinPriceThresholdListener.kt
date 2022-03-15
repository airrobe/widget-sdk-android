package com.airrobe.widgetsdk.airrobewidget.service.listeners

import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeMinPriceThresholdsModel

internal interface AirRobeMinPriceThresholdListener {
    fun onSuccessMinPriceThresholdApi(minPriceThresholdModel: AirRobeMinPriceThresholdsModel)
    fun onFailedMinPriceThresholdApi(error: String? = null)
}