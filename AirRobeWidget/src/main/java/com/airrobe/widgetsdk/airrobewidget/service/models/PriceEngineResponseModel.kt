package com.airrobe.widgetsdk.airrobewidget.service.models

data class PriceEngineResponseModel(
    var engine: String,
    var result: ResultModel?
)

data class ResultModel(
    var hits: HitsModel,
    var resaleValuePercentage: Int,
    var resaleValue: Int?,
    var weight: Double
)

data class HitsModel(
    var brand: Boolean,
    var category: Boolean,
    var material: Boolean
)