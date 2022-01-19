package com.airrobe.widgetsdk.airrobewidget.service.models

data class EmailCheckResponseModel(
    var data: EmailCheckDataModel
)

data class EmailCheckDataModel(
    var isCustomer: Boolean
)