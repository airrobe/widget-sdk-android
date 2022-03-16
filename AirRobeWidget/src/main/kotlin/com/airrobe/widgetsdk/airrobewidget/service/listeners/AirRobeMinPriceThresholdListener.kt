package com.airrobe.widgetsdk.airrobewidget.service.listeners

import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeMinPriceThresholdsModel

internal interface AirRobeMinPriceThresholdListener {
    fun onSuccessMinPriceThresholdsApi(minPriceThresholdModel: AirRobeMinPriceThresholdsModel)
    fun onFailedMinPriceThresholdsApi(error: String? = null)
}