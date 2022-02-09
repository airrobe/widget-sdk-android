package com.airrobe.widgetsdk.airrobewidget.service.listeners

import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel

interface AirRobeGetCategoryMappingListener {
    fun onSuccessGetCategoryMappingApi(categoryModel: CategoryModel)
    fun onFailedGetCategoryMappingApi(error: String? = null)
}