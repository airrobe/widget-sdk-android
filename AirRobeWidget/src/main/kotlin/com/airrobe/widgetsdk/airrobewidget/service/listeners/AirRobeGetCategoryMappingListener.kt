package com.airrobe.widgetsdk.airrobewidget.service.listeners

import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeCategoryModel

internal interface AirRobeGetCategoryMappingListener {
    fun onSuccessGetCategoryMappingApi(categoryModel: AirRobeCategoryModel)
    fun onFailedGetCategoryMappingApi(error: String? = null)
}