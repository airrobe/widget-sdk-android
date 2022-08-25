package com.airrobe.widgetsdk.airrobewidget.service.models

internal data class AirRobeCreateOptedOutOrderModel (
    var data: AirRobeCreateOptedOutOrderDataModel
)

internal data class AirRobeCreateOptedOutOrderDataModel(
    var createOptedOutOrder: AirRobeCreateOptedOutOrderSubModel
)

internal data class AirRobeCreateOptedOutOrderSubModel(
    var created: Boolean,
    var error: String?
)