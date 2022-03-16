package com.airrobe.widgetsdk.airrobewidget.service.models

internal data class AirRobeMinPriceThresholdsModel(
    var data: AirRobeMinPriceDataModel
)

internal data class AirRobeMinPriceDataModel(
    var shop: AirRobeMinPriceShopModel
)

internal data class AirRobeMinPriceShopModel(
    var minimumPriceThresholds: MutableList<AirRobeMinPriceThresholds>
)

internal data class AirRobeMinPriceThresholds(
    var minimumPriceCents: Float,
    var id: Int,
    var department: String,
    var default: Boolean
)