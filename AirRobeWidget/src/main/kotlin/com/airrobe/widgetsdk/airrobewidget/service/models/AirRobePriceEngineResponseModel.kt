package com.airrobe.widgetsdk.airrobewidget.service.models

internal data class PriceEngineResponseModel(
    var engine: String,
    var result: ResultModel?
)

internal data class ResultModel(
    var hits: HitsModel,
    var resaleValuePercentage: Int,
    var resaleValue: Int?,
    var weight: Double
)

internal data class HitsModel(
    var brand: Boolean,
    var category: Boolean,
    var material: Boolean
)