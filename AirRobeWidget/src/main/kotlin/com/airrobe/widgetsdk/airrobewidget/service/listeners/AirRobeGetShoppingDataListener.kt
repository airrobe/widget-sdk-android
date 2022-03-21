package com.airrobe.widgetsdk.airrobewidget.service.listeners

import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeGetShoppingDataModel

internal interface AirRobeGetShoppingDataListener {
    fun onSuccessGetShoppingDataApi(shopModel: AirRobeGetShoppingDataModel)
    fun onFailedGetShoppingDataApi(error: String? = null)
}