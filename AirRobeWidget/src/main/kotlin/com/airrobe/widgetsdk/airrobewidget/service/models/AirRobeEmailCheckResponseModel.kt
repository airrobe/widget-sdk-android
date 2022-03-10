package com.airrobe.widgetsdk.airrobewidget.service.models

internal data class EmailCheckResponseModel(
    var data: EmailCheckDataModel
)

internal data class EmailCheckDataModel(
    var isCustomer: Boolean
)